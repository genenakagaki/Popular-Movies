package com.genenakagaki.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gene on 11/22/16.
 */

public class FetchReviewListTask extends FetchJsonStringTask {

    private static final String LOG_TAG = FetchReviewListTask.class.getSimpleName();
    private static final boolean D = BuildConfig.APP_DEBUG;

    private String mMovieId;
    private View mRootView;
    private String mMovieTitle;

    public FetchReviewListTask(View rootView, String movieId, String movieTitle) {
        mRootView = rootView;
        mMovieId = movieId;
        mMovieTitle = movieTitle;
    }

    @Override
    protected void onPostExecute(String movieJsonString) {
        if (movieJsonString == null) {
            if (D) Log.d(LOG_TAG, "onPostExecute: movie json string is null");
            return;
        }

        if (D) Log.d(LOG_TAG, "onPostExecute: " + movieJsonString);

        LinearLayout reviewList = (LinearLayout)mRootView.findViewById(R.id.review_list);

        final Context context = mRootView.getContext();

        try {
            JSONObject json = new JSONObject(movieJsonString);
            JSONArray results = json.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);

                final String author  = result.getString("author");
                final String content = result.getString("content");

                View reviewView = LayoutInflater
                        .from(context)
                        .inflate(R.layout.list_item_review, reviewList, false);

                TextView reviewAuthor = (TextView) reviewView.findViewById(R.id.review_author_textview);
                reviewAuthor.setText(author);

                Button readButton = (Button) reviewView.findViewById(R.id.read_button);

                readButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = mRootView.getContext();
                        Intent reviewIntent = new Intent(context, ReviewActivity.class);
                        reviewIntent.putExtra(ReviewActivity.MOVIE_TITLE_EXTRA_KEY, mMovieTitle);
                        reviewIntent.putExtra(ReviewActivity.REVIEW_AUTHOR_EXTRA_KEY, author);
                        reviewIntent.putExtra(ReviewActivity.REVIEW_CONTENT_EXTRA_KEY, content);
                        context.startActivity(reviewIntent);
                    }
                });

                reviewList.addView(reviewView);
                reviewList.addView(createHorizontalLineView());
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

        String preBuildUri = BASE_URL + "/" + mMovieId + "/reviews";

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
