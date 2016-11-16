package com.genenakagaki.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gene on 11/11/16.
 */

public class FetchMovieTask extends FetchJsonStringTask {

    private static final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private static final boolean D = BuildConfig.APP_DEBUG;

    private String mMovieId;
    private View mRootView;

    public FetchMovieTask(View rootView, String movieId) {
        mMovieId = movieId;
        mRootView = rootView;
    }

    @Override
    protected void onPostExecute(String movieJsonString) {
        if (movieJsonString == null) {
            if (D) Log.d(LOG_TAG, "onPostExecute: movie json string is null");
            return;
        }

        if (D) Log.d(LOG_TAG, "onPostExecute: " + movieJsonString);

        try {
            JSONObject movie = new JSONObject(movieJsonString);

            String title       = movie.getString("title");
            String posterPath  = movie.getString("poster_path");
            String releaseDate = movie.getString("release_date").substring(0, 4);
            String runtime     = movie.getString("runtime") + "min";
            String rating      = movie.getString("vote_average") + "/10";
            String plot        = movie.getString("overview");

            ((TextView)mRootView.findViewById(R.id.movie_title_textview)).setText(title);
            ((TextView)mRootView.findViewById(R.id.movie_date_textview)).setText(releaseDate);
            ((TextView)mRootView.findViewById(R.id.movie_runtime_textview)).setText(runtime);
            ((TextView)mRootView.findViewById(R.id.movie_rating_textview)).setText(rating);
            ((TextView)mRootView.findViewById(R.id.movie_plot_textview)).setText(plot);

            ImageView imageView = (ImageView) mRootView.findViewById(R.id.movie_poster_imageview);
            Utils.setMoviePosterImage(mRootView.getContext(), imageView, posterPath);

        } catch (JSONException e) {
            if (D) Log.d(LOG_TAG, "Error while handling JSON string.", e);
        }
    }

    @Override
    public URL createRequestUrl() {
        // Create url request for themoviedb
        final String BASE_URL = "https://api.themoviedb.org/3/movie";
        final String API_PARAM = "api_key";

        String preBuildUri = BASE_URL + "/" + mMovieId;

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
