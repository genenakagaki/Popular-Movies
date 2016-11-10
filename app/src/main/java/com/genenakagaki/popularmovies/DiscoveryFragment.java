package com.genenakagaki.popularmovies;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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
        mGridView.setAdapter(mImageAdapter);

        updateSortOrder();

        return rootView;
    }

    public void updateSortOrder() {
        new FetchMovieTask(getActivity(), mGridView).execute();
    }
}
