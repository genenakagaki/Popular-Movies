package com.genenakagaki.popularmovies.discovery;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.genenakagaki.popularmovies.BuildConfig;
import com.genenakagaki.popularmovies.ImageAdapter;
import com.genenakagaki.popularmovies.R;
import com.genenakagaki.popularmovies.Utils;
import com.genenakagaki.popularmovies.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DiscoveryFragment extends Fragment {

    private static final String LOG_TAG = DiscoveryFragment.class.getSimpleName();
    private static final boolean D = BuildConfig.APP_DEBUG;

    private ImageAdapter mImageAdapter;
    private GridView mGridView;

    public DiscoveryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (D) Log.d(LOG_TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_discovery, container, false);

        mImageAdapter = new ImageAdapter(getActivity());

        mGridView = (GridView) rootView.findViewById(R.id.discovery_gridview);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridView.setNumColumns(3);
        }

        mGridView.setAdapter(mImageAdapter);

        updateSortOrder();

        return rootView;
    }

    public void updateSortOrder() {
        Context context = getActivity();

        String sortOrder = Utils.getSortOrder(context);

        if (sortOrder.equals(context.getString(R.string.pref_sort_order_value_favorite))) {
            Cursor cursor = context.getContentResolver().query(
                    MovieContract.FavoriteEntry.CONTENT_URI,
                    MovieContract.FavoriteEntry.PROJECTION,
                    null,
                    null,
                    MovieContract.FavoriteEntry.COLUMN_DATE + " DESC"
            );

            mImageAdapter.clear();
            mImageAdapter.add(cursor);
        } else {
            new FetchMovieListTask(context, mGridView, sortOrder).execute();
        }
    }
}
