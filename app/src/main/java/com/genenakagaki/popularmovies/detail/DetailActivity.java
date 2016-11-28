package com.genenakagaki.popularmovies.detail;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.genenakagaki.popularmovies.BuildConfig;
import com.genenakagaki.popularmovies.R;
import com.genenakagaki.popularmovies.data.MovieContract;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private static final boolean D = BuildConfig.APP_DEBUG;

    public static final String MOVIE_ID_EXTRA_KEY = "movie_id_extra_key";
    public static final String POSTER_PATH_EXTRA_KEY = "poster_path_extra_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            String movieId = getIntent().getStringExtra(MOVIE_ID_EXTRA_KEY);
            String posterPath = getIntent().getStringExtra(POSTER_PATH_EXTRA_KEY);

            boolean isFavorite = false;
            Cursor cursor = getContentResolver().query(
                    MovieContract.FavoriteEntry.CONTENT_URI,
                    MovieContract.FavoriteEntry.PROJECTION,
                    MovieContract.FavoriteEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[] {movieId},
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                isFavorite = true;
            }

            if (D) Log.d(LOG_TAG, "isFavorite: " + isFavorite);

            DetailFragment detailFragment = DetailFragment.newInstance(movieId, posterPath, isFavorite);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_fragment_container, detailFragment)
                    .commit();
        }
    }
}
