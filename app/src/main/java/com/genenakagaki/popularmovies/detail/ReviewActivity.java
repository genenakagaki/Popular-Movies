package com.genenakagaki.popularmovies.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.genenakagaki.popularmovies.BuildConfig;
import com.genenakagaki.popularmovies.R;

/**
 * Created by gene on 11/23/16.
 */

public class ReviewActivity extends AppCompatActivity {

    private static final String LOG_TAG = ReviewActivity.class.getSimpleName();
    private static final boolean D = BuildConfig.APP_DEBUG;

    public static final String MOVIE_TITLE_EXTRA_KEY = "movie_title_extra_key";
    public static final String REVIEW_AUTHOR_EXTRA_KEY = "review_author_extra_key";
    public static final String REVIEW_CONTENT_EXTRA_KEY = "movie_id_extra_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            String movieTitle    = intent.getStringExtra(MOVIE_TITLE_EXTRA_KEY);
            String reviewAuthor  = intent.getStringExtra(REVIEW_AUTHOR_EXTRA_KEY);
            String reviewContent = intent.getStringExtra(REVIEW_CONTENT_EXTRA_KEY);
            ReviewFragment reviewFragment = ReviewFragment
                    .newInstance(movieTitle, reviewAuthor, reviewContent);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.review_fragment_container, reviewFragment)
                    .commit();
        }
    }
}
