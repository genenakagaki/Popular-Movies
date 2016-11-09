package com.genenakagaki.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gene on 11/8/16.
 */

public class FetchMovieTask extends AsyncTask<Void, Void, String> {

    private static final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private static final boolean D = BuildConfig.APP_DEBUG;

    private ImageAdapter mImageAdapter;

    public FetchMovieTask(ImageAdapter imageAdapter) {
        mImageAdapter = imageAdapter;
    }

    @Override
    protected String doInBackground(Void... params) {
        if (D) Log.d(LOG_TAG, "doInBackground");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieJsonStr = null;

        try {
            // Create url request for themoviedb
            final String BASE_URL = "https://api.themoviedb.org/3/movie";
            final String POPULAR_URL = BASE_URL + "/popular";
            final String API_PARAM = "api_key";

            Uri uri = Uri.parse(POPULAR_URL).buildUpon()
                    .appendQueryParameter(API_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .build();

            URL url = new URL(uri.toString());

            // open connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) return null;

            StringBuilder sBuilder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }

            if (sBuilder.length() == 0) return null;

            movieJsonStr = sBuilder.toString();


        } catch (IOException e) {
            if (D) Log.d(LOG_TAG, "Error while creating a request url for themoviedb.", e);
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

        if (D && movieJsonStr != null) Log.d(LOG_TAG, "url request result: " + movieJsonStr);
        return movieJsonStr;
    }

    @Override
    protected void onPostExecute(String movieJsonString) {
        if (D) Log.d(LOG_TAG, "onPostExecute: " + movieJsonString);

        // create list of imageURLs
        List<String> imageUrls = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(movieJsonString);
            JSONArray results = json.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                imageUrls.add(result.getString("poster_path"));
            }

        } catch (JSONException e) {
            if (D) Log.d(LOG_TAG, "Error while handling JSON string.", e);
        }

        // update ImageAdapter
        mImageAdapter.clear();
        mImageAdapter.addAll(imageUrls);
    }
}
