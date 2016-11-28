package com.genenakagaki.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by gene on 11/23/16.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.genenakagaki.popularmovies.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITE = "favorite";

    public static final class FavoriteEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE
                        + "/" + CONTENT_AUTHORITY
                        + "/" + PATH_FAVORITE;

        public static final String TABLE_NAME = "favorite";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_DATE = "date";

        public static Uri buildFavoriteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String[] PROJECTION = new String[] {
                FavoriteEntry.COLUMN_MOVIE_ID,
                FavoriteEntry.COLUMN_POSTER_PATH,
                FavoriteEntry.COLUMN_DATE
        };

        public static final int INDEX_MOVIE_ID = 0;
        public static final int INDEX_POSTER_PATH = 1;
        public static final int INDEX_DATE = 2;
    }

}
