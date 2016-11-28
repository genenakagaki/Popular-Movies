package com.genenakagaki.popularmovies.discovery;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.GridView;

import com.genenakagaki.popularmovies.BuildConfig;
import com.genenakagaki.popularmovies.FetchJsonStringTask;
import com.genenakagaki.popularmovies.ImageAdapter;
import com.genenakagaki.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gene on 11/8/16.
 */

public class FetchMovieListTask extends FetchJsonStringTask {

    private static final String LOG_TAG = FetchMovieListTask.class.getSimpleName();
    private static final boolean D = BuildConfig.APP_DEBUG;

    private Context mContext;
    private GridView mGridView;
    private String mSortOrder;

    public interface Callback {
        void onFetchMovieListTaskFinished(String movieId, String posterPath);
    }

    public FetchMovieListTask(Context context, GridView gridView, String sortOrder) {
        mContext = context;
        mGridView = gridView;
        mSortOrder = sortOrder;
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

        if (!imageAdapter.isEmpty()) {
            ((Callback)mContext).onFetchMovieListTaskFinished(
                    imageAdapter.getMovieId(0),
                    imageAdapter.getImageUrl(0));
        }
    }

    @Override
    public URL createRequestUrl() {
        // Create url request for themoviedb
        final String BASE_URL = "https://api.themoviedb.org/3/movie";
        final String POPULAR_URL = "/popular";
        final String RATING_URL = "/top_rated";
        final String API_PARAM = "api_key";

        String preBuildUri;

        if (mSortOrder.equals(mContext.getString(R.string.pref_sort_order_value_popularity))) {
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
