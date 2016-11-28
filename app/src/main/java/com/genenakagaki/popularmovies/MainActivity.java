package com.genenakagaki.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.genenakagaki.popularmovies.detail.DetailActivity;
import com.genenakagaki.popularmovies.detail.DetailFragment;
import com.genenakagaki.popularmovies.discovery.DiscoveryFragment;
import com.genenakagaki.popularmovies.discovery.FetchMovieListTask;
import com.genenakagaki.popularmovies.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity
        implements DiscoveryFragment.Callback,
                   FetchMovieListTask.Callback {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final boolean D = BuildConfig.APP_DEBUG;

    private String mSortOrder;

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View detailContainer = findViewById(R.id.fragment_detail_container);
        if (detailContainer == null) {
            mTwoPane = false;
            if (D) Log.d(LOG_TAG, "Device is mobile.");
        } else {
            mTwoPane = true;
            if (D) Log.d(LOG_TAG, "Device is tablet.");
        }
    }

    @Override
    protected void onResume() {
        if (D) Log.d(LOG_TAG, "onResume");
        super.onResume();

        String sortOrder = Utils.getSortOrder(this);

        if (mSortOrder != sortOrder) {
            mSortOrder = sortOrder;

            if (mSortOrder.equals(getString(R.string.pref_sort_order_value_popularity))) {
                getSupportActionBar().setTitle(getString(R.string.activity_main_popular_label));
            } else if (mSortOrder.equals(getString(R.string.pref_sort_order_value_rating))) {
                getSupportActionBar().setTitle(getString(R.string.activity_main_top_rated_label));
            } else {
                getSupportActionBar().setTitle(getString(R.string.activity_main_favorite_label));
            }

            DiscoveryFragment discoveryFragment =
                    (DiscoveryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_discovery);
            if (discoveryFragment != null) {
                discoveryFragment.updateSortOrder();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String movieId, String posterPath) {
        if (mTwoPane) {
            // replace the detail fragment with the selected movie
            DetailFragment detailFragment = DetailFragment.newInstance(
                    movieId,
                    posterPath,
                    Utils.isFavoriteMovie(this, movieId));

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_detail_container, detailFragment)
                    .commit();
        } else {
            // start detail activity
            Intent detailIntent = new Intent(this, DetailActivity.class);
            detailIntent.putExtra(DetailActivity.MOVIE_ID_EXTRA_KEY, movieId);
            detailIntent.putExtra(DetailActivity.POSTER_PATH_EXTRA_KEY, posterPath);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onFetchMovieListTaskFinished(String movieId, String posterPath) {
        if (D) Log.d(LOG_TAG, "onFetchMovieListTaskFinished");

        if (mTwoPane) {
            DetailFragment detailFragment = (DetailFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_detail_container);

            if (detailFragment == null) {
                // if detail pane is empty, show the detail of the first movie

                detailFragment = DetailFragment.newInstance(
                        movieId,
                        posterPath,
                        Utils.isFavoriteMovie(this, movieId));

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_detail_container, detailFragment)
                        .commit();
            }

        }
    }
}
