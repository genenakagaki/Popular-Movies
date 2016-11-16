package com.genenakagaki.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gene on 11/8/16.
 */

public class FetchMovieListTask extends AsyncTask<Void, Void, String> {

    private static final String LOG_TAG = FetchMovieListTask.class.getSimpleName();
    private static final boolean D = BuildConfig.APP_DEBUG;

    private Context mContext;
    private GridView mGridView;

    public FetchMovieListTask(Context context, GridView gridView) {
        mContext = context;
        mGridView = gridView;
    }

    @Override
    protected String doInBackground(Void... params) {
        if (D) Log.d(LOG_TAG, "doInBackground");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsonStr = null;

        URL requestUrl = createRequestUrl();
        if (requestUrl == null) {
            if (D) Log.d(LOG_TAG, "requestUrl is null");
            return null;
        }

        try {
            // open connection
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                if (D) Log.d(LOG_TAG, "inputStream is null");
                return null;
            }

            StringBuilder sBuilder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }

            if (sBuilder.length() == 0) {
                if (D) Log.d(LOG_TAG, "inputStream is empty");
                return null;
            }

            movieJsonStr = sBuilder.toString();

        } catch (IOException e) {
            if (D) Log.d(LOG_TAG, "Error reading input stream", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    if (D) Log.d(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return movieJsonStr;
    }

    @Override
    protected void onPostExecute(String movieJsonString) {
        if (movieJsonString == null) {
            if (D) Log.d(LOG_TAG, "onPostExecute: movie json string is null");
            return;
        }

        if (D) Log.d(LOG_TAG, "onPostExecute: " + movieJsonString);

        ImageAdapter imageAdapter = (ImageAdapter) mGridView.getAdapter();
        imageAdapter.clear();

        try {
            JSONObject json = new JSONObject(movieJsonString);
            JSONArray results = json.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                imageAdapter.add(result.getString("id"), result.getString("poster_path"));
            }

        } catch (JSONException e) {
            if (D) Log.d(LOG_TAG, "Error while handling JSON string.", e);
        }

        mGridView.invalidateViews();
    }

    private URL createRequestUrl() {
        // Create url request for themoviedb
        final String BASE_URL = "https://api.themoviedb.org/3/movie";
        final String POPULAR_URL = "/popular";
        final String RATING_URL = "/top_rated";
        final String API_PARAM = "api_key";

        String preBuildUri;

        if (Utils.isOrderedByPopularity(mContext)) {
            preBuildUri = BASE_URL + POPULAR_URL;
        } else {
            preBuildUri = BASE_URL + RATING_URL;
        }

        Uri uri = Uri.parse(preBuildUri).buildUpon()
                .appendQueryParameter(API_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .build();

        if (D) Log.d(LOG_TAG, "requestUrl: " + uri.toString());

        URL url = null;

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            if (D) Log.d(LOG_TAG, "Error creating request url for themoviedb", e);
        }

        return url;
    }
}
