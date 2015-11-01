package com.focusandcode.popularmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Moise2022 on 10/25/15.
 */
public class MoviesDBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = MoviesDBHelper.class.getName();

    //name & version
    private static final String DATABASE_NAME = "fevorite_movies.db";
    private static final int DATABASE_VERSION = 1;

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Create the database
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MoviesContract.MovieEntry.TABLE_MOVIE + "(" + MoviesContract.MovieEntry._ID +
                " INTEGER PRIMARY KEY, " +
                MoviesContract.MovieEntry.COLUMN_ADULT + " INTEGER NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH + " TEXT,  " +
                MoviesContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE  + " TEXT, " +
                MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MoviesContract.MovieEntry.COLUMN_OVERVIEW  + " TEXT, " +
                MoviesContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                MoviesContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MoviesContract.MovieEntry.COLUMN_POPULARITY + " TEXT, " +
                MoviesContract.MovieEntry.COLUMN_TITLE + " TEXT, " +
                MoviesContract.MovieEntry.COLUMN_VIDEO + " INTEGER NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE + " NUMERIC, " +
                MoviesContract.MovieEntry.COLUMN_VOTE_COUNT +
                        " INTEGER NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    // Upgrade database when version is changed.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MovieEntry.TABLE_MOVIE);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                MoviesContract.MovieEntry.TABLE_MOVIE + "'");


        // re-create database
        onCreate(sqLiteDatabase);
    }
}