package com.genenakagaki.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import static android.R.attr.fragment;
import static android.R.attr.matchOrder;
import static com.genenakagaki.popularmovies.Utils.isOrderedByPopularity;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final boolean D = BuildConfig.APP_DEBUG;

    private boolean mIsOrderedByPopularity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        if (D) Log.d(LOG_TAG, "onResume");
        super.onResume();

        boolean isOrderedByPopularity = Utils.isOrderedByPopularity(this);

        if (mIsOrderedByPopularity != isOrderedByPopularity) {
            mIsOrderedByPopularity = isOrderedByPopularity;

            if (mIsOrderedByPopularity) {
                getSupportActionBar().setTitle(getString(R.string.activity_main_popular_label));
            } else {
                getSupportActionBar().setTitle(getString(R.string.activity_main_top_rated_label));
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
}
