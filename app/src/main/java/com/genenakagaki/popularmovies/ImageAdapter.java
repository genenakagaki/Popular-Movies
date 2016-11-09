package com.genenakagaki.popularmovies;

import android.content.Context;
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

    private List<String> mImageUrls;

    public ImageAdapter(Context context) {
        mContext = context;
        mImageUrls = new ArrayList<>();

        mImageUrls.add("/xfWac8MTYDxujaxgPVcRD9yZaul.jpg");
        mImageUrls.add("/5N20rQURev5CNDcMjHVUZhpoCNC.jpg");
        mImageUrls.add("/kqjL17yufvn9OVLyXYpvtyrFfak.jpg");
        mImageUrls.add("/mLrQMqyZgLeP8FrT5LCobKAiqmK.jpg");
    }

    @Override
    public int getCount() {
        return mImageUrls.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setAdjustViewBounds(true);

        } else {
            imageView = (ImageView) convertView;
        }

        final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185";

        Picasso.with(mContext).load(BASE_IMAGE_URL + mImageUrls.get(position)).into(imageView);
        return imageView;
    }

    public void clear() {
        mImageUrls.clear();
    }

    public void add(String imageUrl) {
        mImageUrls.add(imageUrl);
    }

    public void addAll(List<String> list) {
        mImageUrls.addAll(list);
    }
}
