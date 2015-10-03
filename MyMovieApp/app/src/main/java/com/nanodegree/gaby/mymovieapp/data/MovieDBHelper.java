package com.nanodegree.gaby.mymovieapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gaby on 9/26/2015.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MovieContract.MovieEntry.SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL("PRAGMA foreign_keys = ON;");
        sqLiteDatabase.execSQL(MovieContract.ReviewEntry.SQL_CREATE_REVIEW_TABLE);
        sqLiteDatabase.execSQL(MovieContract.VideoEntry.SQL_CREATE_VIDEO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
