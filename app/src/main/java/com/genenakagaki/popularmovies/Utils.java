package com.genenakagaki.popularmovies;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by gene on 11/9/16.
 */

public class Utils {

    private static final String LOG_TAG = Utils.class.getSimpleName();
    private static final boolean D = BuildConfig.APP_DEBUG;

    public static boolean isOrderedByPopularity(Context context) {
        String sortOrder = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(
                        context.getString(R.string.pref_sort_order_key),
                        "");

        if (D) Log.d(LOG_TAG, "isOrderedByPopularity\n"
                + "   sortOrder = " + sortOrder);

        return sortOrder.equals(context.getString(R.string.pref_sort_order_value_popularity));
    }
}
