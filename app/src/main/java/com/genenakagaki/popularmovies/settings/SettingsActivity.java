package com.genenakagaki.popularmovies.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.genenakagaki.popularmovies.settings.SettingsFragment;

/**
 * Created by gene on 11/9/16.
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
