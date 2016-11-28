package com.genenakagaki.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLClientInfoException;

import static com.genenakagaki.popularmovies.data.MovieContract.*;

/**
 * Created by gene on 11/23/16.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " ("
                + FavoriteEntry._ID + " INTEGER PRIMARY KEY, "
                + FavoriteEntry.COLUMN_MOVIE_ID + " TEXT UNIQUE NOT NULL, "
                + FavoriteEntry.COLUMN_POSTER_PATH + " TEXT UNIQUE NOT NULL, "
                + FavoriteEntry.COLUMN_DATE + " INTEGER NOT NULL" + ");";

        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
