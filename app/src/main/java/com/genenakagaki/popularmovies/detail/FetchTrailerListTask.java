package com.genenakagaki.popularmovies.detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genenakagaki.popularmovies.BuildConfig;
import com.genenakagaki.popularmovies.FetchJsonStringTask;
import com.genenakagaki.popularmovies.R;
import com.genenakagaki.popularmovies.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gene on 11/16/16.
 */

public class FetchTrailerListTask extends FetchJsonStringTask {

    private static final String LOG_TAG = FetchTrailerListTask.class.getSimpleName();
    private static final boolean D = BuildConfig.APP_DEBUG;

    private View mRootView;
    private String mMovieId;

    public FetchTrailerListTask(View rootView, String movieId) {
        mRootView = rootView;
        mMovieId = movieId;
    }

    @Override
    protected void onPostExecute(String movieJsonString) {
        if (movieJsonString == null) {
            if (D) Log.d(LOG_TAG, "onPostExecute: movie json string is null");
            return;
        }

        if (D) Log.d(LOG_TAG, "onPostExecute: " + movieJsonString);

        LinearLayout trailerList = (LinearLayout)mRootView.findViewById(R.id.trailer_list);

        final Context context = mRootView.getContext();

        final String VIDEO_BASE_URL = "https://www.youtube.com/watch?v=";

        try {
            JSONObject json = new JSONObject(movieJsonString);
            JSONArray results = json.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);

                final String url = VIDEO_BASE_URL + result.getString("key");
                String title = result.getString("name");

                View trailerView = LayoutInflater
                        .from(context)
                        .inflate(R.layout.list_item_trailer, trailerList, false);

                ImageButton playButton = (ImageButton)trailerView.findViewById(R.id.trailer_play_button);
                playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }
                });

                TextView trailerTitle = (TextView)trailerView.findViewById(R.id.trailer_title);
                trailerTitle.setText(title);

                trailerList.addView(trailerView);
                trailerList.addView(createHorizontalLineView());
            }

            if (results.length() == 0) {
                int padding = (int) Utils.convertToPx(context, 8);

                TextView textView = new TextView(context);
                textView.setText(context.getString(R.string.detail_no_trailer_label));
                textView.setPadding(padding, 0, 0, padding);
                trailerList.addView(textView);
            }

        } catch (JSONException e) {
            if (D) Log.d(LOG_TAG, "Error while handling JSON string.", e);
        }
    }

    @Override
    public URL createRequestUrl() {
        // Create url request for themoviedb
        final String BASE_URL = "https://api.themoviedb.org/3/movie";
        final String API_PARAM = "api_key";

        String preBuildUri = BASE_URL + "/" + mMovieId + "/videos";

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

    private View createHorizontalLineView() {
        Context context = mRootView.getContext();

        View lineView = new View(mRootView.getContext());
        int dpValue = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1,
                context.getResources().getDisplayMetrics());
        lineView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpValue
        ));
        lineView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray));

        dpValue = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16,
                context.getResources().getDisplayMetrics());
        lineView.setPadding(dpValue, 0, dpValue, 0);

        return lineView;
    }
}
