package com.genenakagaki.popularmovies;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by gene on 11/9/16.
 */

public class Utils {

    private static final String LOG_TAG = Utils.class.getSimpleName();
    private static final boolean D = BuildConfig.APP_DEBUG;

    private static final String BASE_MOVIE_POSTER_URL = "http://image.tmdb.org/t/p/w185";

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

    public static void setMoviePosterImage(Context context, ImageView imageView, String posterPath) {
        Picasso.with(context).load(BASE_MOVIE_POSTER_URL + posterPath).into(imageView);
    }
}
