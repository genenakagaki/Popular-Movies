package com.genenakagaki.popularmovies.detail;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.genenakagaki.popularmovies.BuildConfig;
import com.genenakagaki.popularmovies.MainActivity;
import com.genenakagaki.popularmovies.R;
import com.genenakagaki.popularmovies.Utils;
import com.genenakagaki.popularmovies.data.MovieContract;
import com.genenakagaki.popularmovies.discovery.DiscoveryFragment;

import java.util.Calendar;

import static com.genenakagaki.popularmovies.data.MovieContract.*;

public class DetailFragment extends Fragment {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final boolean D = BuildConfig.APP_DEBUG;

    private static final String ARG_MOVIE_ID_PARAM = "arg_movie_id_param";
    private static final String ARG_POSTER_PATH_PARAM = "arg_poster_path_param";
    private static final String ARG_FAVORITE_PARAM = "arg_favorite_param";

    private String mMovieId;
    private String mPosterPath;
    private boolean mIsFavorite;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(String movieId, String posterPath, boolean isFavorite) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOVIE_ID_PARAM, movieId);
        args.putString(ARG_POSTER_PATH_PARAM, posterPath);
        args.putBoolean(ARG_FAVORITE_PARAM, isFavorite);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (getArguments() != null) {
            mMovieId = args.getString(ARG_MOVIE_ID_PARAM);
            mPosterPath = args.getString(ARG_POSTER_PATH_PARAM);
            mIsFavorite = args.getBoolean(ARG_FAVORITE_PARAM);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (D) Log.d(LOG_TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        new FetchMovieTask(rootView, mMovieId).execute();
        new FetchTrailerListTask(rootView, mMovieId).execute();

        ImageView imageView = (ImageView) rootView.findViewById(R.id.movie_poster_imageview);
        Utils.setMoviePosterImage(rootView.getContext(), imageView, mPosterPath);

        final Button favoriteButton = (Button) rootView.findViewById(R.id.favorite_button);
        if (mIsFavorite) {
            favoriteButton.setText(getActivity().getString(R.string.detail_unfavorite_button));
            favoriteButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            favoriteButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.dark_gray));
        }

        favoriteButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (D) Log.d(LOG_TAG, "Favorite button clicked");

                if (mIsFavorite) {
                    getActivity().getContentResolver().delete(
                            FavoriteEntry.CONTENT_URI,
                            FavoriteEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[] {mMovieId}
                    );

                    favoriteButton.setText(getActivity().getString(R.string.detail_favorite_button));
                    favoriteButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    favoriteButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.favorite_button_background));

                    mIsFavorite = false;
                } else {
                    Calendar calendar = Calendar.getInstance();
                    long currentDate = calendar.getTimeInMillis();

                    ContentValues favoriteMovieValues = new ContentValues();
                    favoriteMovieValues.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_ID, mMovieId);
                    favoriteMovieValues.put(MovieContract.FavoriteEntry.COLUMN_POSTER_PATH, mPosterPath);
                    favoriteMovieValues.put(MovieContract.FavoriteEntry.COLUMN_DATE, currentDate);

                    getActivity().getContentResolver().insert(FavoriteEntry.CONTENT_URI, favoriteMovieValues);

                    favoriteButton.setText(getActivity().getString(R.string.detail_unfavorite_button));
                    favoriteButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    favoriteButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.dark_gray));

                    mIsFavorite = true;
                }

                if (getActivity() instanceof MainActivity) {
                    if (D) Log.d(LOG_TAG, "Device is tablet.");

                    String sortOrder = Utils.getSortOrder(getActivity());
                    String favoriteSortOrder = getActivity().getString(R.string.pref_sort_order_value_favorite);

                    if (sortOrder.equals(favoriteSortOrder)) {
                        if (D) Log.d(LOG_TAG, "Discovery page is showing favorite movies.");
                        DiscoveryFragment discoveryFragment =
                                (DiscoveryFragment) getActivity().getSupportFragmentManager()
                                        .findFragmentById(R.id.fragment_discovery);
                        if (discoveryFragment != null) {
                            discoveryFragment.updateSortOrder();
                        }
                    }

                }
            }
        });


        return rootView;
    }

}
