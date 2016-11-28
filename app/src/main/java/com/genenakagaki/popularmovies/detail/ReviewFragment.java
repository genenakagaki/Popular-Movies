package com.genenakagaki.popularmovies.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.genenakagaki.popularmovies.BuildConfig;
import com.genenakagaki.popularmovies.R;

/**
 * Created by gene on 11/23/16.
 */

public class ReviewFragment extends Fragment {

    private static final String LOG_TAG = ReviewFragment.class.getSimpleName();
    private static final boolean D = BuildConfig.APP_DEBUG;

    private static final String ARG_MOVIE_TITLE_PARAM    = "arg_movie_title_param";
    private static final String ARG_REVIEW_AUTHOR_PARAM  = "arg_review_author_param";
    private static final String ARG_REVIEW_CONTENT_PARAM = "arg_review_content_param";

    private String mMovieTitle;
    private String mReviewAuthor;
    private String mReviewContent;

    public ReviewFragment() {
        // empty constructor required
    }

    public static ReviewFragment newInstance(String movieTitle, String reviewAuthor, String reviewContent) {

        Bundle args = new Bundle();
        args.putString(ARG_MOVIE_TITLE_PARAM, movieTitle);
        args.putString(ARG_REVIEW_AUTHOR_PARAM, reviewAuthor);
        args.putString(ARG_REVIEW_CONTENT_PARAM, reviewContent);

        ReviewFragment fragment = new ReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mMovieTitle    = args.getString(ARG_MOVIE_TITLE_PARAM);
            mReviewAuthor  = args.getString(ARG_REVIEW_AUTHOR_PARAM);
            mReviewContent = args.getString(ARG_REVIEW_CONTENT_PARAM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);

        TextView movieTitle = (TextView)rootView.findViewById(R.id.movie_title_textview);
        movieTitle.setText(mMovieTitle);

        TextView reviewAuthor = (TextView) rootView.findViewById(R.id.review_author_textview);
        reviewAuthor.setText("Author: " + mReviewAuthor);

        TextView reviewContent = (TextView)rootView.findViewById(R.id.review_content_textview);
        reviewContent.setText(mReviewContent);

        return rootView;
    }
}
