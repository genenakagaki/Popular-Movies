package com.genenakagaki.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gene on 11/8/16.
 */

public class ImageAdapter extends BaseAdapter {

    private static final String LOG_TAG = ImageAdapter.class.getSimpleName();
    private static final boolean D = BuildConfig.APP_DEBUG;

    private Context mContext;

    private List<String> mMovieIds;
    private List<String> mImageUrls;

    public ImageAdapter(Context context) {
        mContext = context;
        mMovieIds = new ArrayList<>();
        mImageUrls = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mMovieIds.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }

        Utils.setMoviePosterImage(mContext, imageView, mImageUrls.get(position));

        final String movieId = mMovieIds.get(position);
        imageView.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(mContext, DetailActivity.class);
                detailIntent.putExtra(DetailActivity.MOVIE_ID_EXTRA_KEY, movieId);
                mContext.startActivity(detailIntent);
            }
        });

        return imageView;
    }

    public void clear() {
        mMovieIds.clear();
        mImageUrls.clear();
    }

    public void add(String movieId, String imageUrl) {
        mMovieIds.add(movieId);
        mImageUrls.add(imageUrl);
    }
}
