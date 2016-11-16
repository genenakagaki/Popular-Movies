package com.genenakagaki.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private static final boolean D = BuildConfig.APP_DEBUG;

    public static final String MOVIE_ID_EXTRA_KEY = "movie_id_extra_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            DetailFragment detailFragment = DetailFragment.newInstance(getIntent().getStringExtra(MOVIE_ID_EXTRA_KEY));

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_fragment_container, detailFragment)
                    .commit();
        }
    }
}
