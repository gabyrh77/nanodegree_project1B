package com.nanodegree.gaby.mymovieapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Gaby on 9/26/2015.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.nanodegree.gaby.mymovieapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_VIDEO = "video";

    public static final class MovieEntry implements BaseColumns {
        //table name
        public static final String TABLE_NAME = "movie";

        //columns
        public static final String COLUMN_MOVIE_IMDB_ID = "movie_imdb_id";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_POSTER = "movie_poster";
        public static final String COLUMN_MOVIE_RATING = "movie_rating";
        public static final String COLUMN_MOVIE_POPULARITY = "movie_popularity";
        public static final String COLUMN_MOVIE_SYNOPSIS = "movie_synopsis";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movie_release_date";
        public static final String COLUMN_MOVIE_FAVORITE = "movie_favorite";

        //table create query
        public static final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_MOVIE_IMDB_ID + " LONG UNIQUE NOT NULL, " +
                COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                COLUMN_MOVIE_POSTER + " TEXT, " +
                COLUMN_MOVIE_RATING + " REAL DEFAULT 0, " +
                COLUMN_MOVIE_POPULARITY + " REAL DEFAULT 0, " +
                COLUMN_MOVIE_SYNOPSIS + " TEXT DEFAULT '', " +
                COLUMN_MOVIE_RELEASE_DATE + " TEXT DEFAULT '', " +
                COLUMN_MOVIE_FAVORITE + " INTEGER DEFAULT 0" +
                " );";

        //Cursors
        public static final String[] DETAIL_COLUMNS = {
                MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
                MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS,
                MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,
                MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY,
                MovieContract.MovieEntry.COLUMN_MOVIE_RATING,
                MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
                MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE
        };

        public static final int COLUMN_MOVIE_TITLE_INDEX = 1;
        public static final int COLUMN_MOVIE_SYNOPSIS_INDEX = 2;
        public static final int COLUMN_MOVIE_POSTER_INDEX = 3;
        public static final int COLUMN_MOVIE_POPULARITY_INDEX = 4;
        public static final int COLUMN_MOVIE_RATING_INDEX = 5;
        public static final int COLUMN_MOVIE_RELEASE_DATE_INDEX = 6;
        public static final int COLUMN_MOVIE_FAVORITE_INDEX = 7;

        //Uri
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ReviewEntry implements BaseColumns{
        //table name
        public static final String TABLE_NAME = "review";

        //columns
        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_MOVIE_ID = "review_movie_id";
        public static final String COLUMN_REVIEW_AUTHOR = "review_author";
        public static final String COLUMN_REVIEW_CONTENT = "review_content";
        public static final String COLUMN_REVIEW_URL = "review_url";

        //table create query
        public static final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_REVIEW_ID + " TEXT UNIQUE NOT NULL, " +
                COLUMN_MOVIE_ID + " LONG REFERENCES " + MovieEntry.TABLE_NAME + " ("+MovieEntry.COLUMN_MOVIE_IMDB_ID + ") ON UPDATE CASCADE," +
                COLUMN_REVIEW_AUTHOR + " TEXT DEFAULT '', " +
                COLUMN_REVIEW_CONTENT + " TEXT NOT NULL, " +
                COLUMN_REVIEW_URL + " TEXT" +
                " );";

        //CURSORS
        public static final String[] REVIEW_COLUMNS = {
                MovieContract.ReviewEntry._ID,
                MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT,
                MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHOR,
        };
        public static final int COLUMN_REVIEW_CONTENT_INDEX = 1;
        public static final int COLUMN_REVIEW_AUTHOR_INDEX = 2;

        //Uri
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class VideoEntry implements BaseColumns{
        //table name
        public static final String TABLE_NAME = "video";

        //columns
        public static final String COLUMN_VIDEO_ID = "video_id";
        public static final String COLUMN_MOVIE_ID = "video_movie_id";
        public static final String COLUMN_VIDEO_LANGUAGE = "video_language";
        public static final String COLUMN_VIDEO_KEY = "video_key";
        public static final String COLUMN_VIDEO_NAME = "video_name";
        public static final String COLUMN_VIDEO_SITE = "video_site";
        public static final String COLUMN_VIDEO_TYPE = "video_type";

        //table create query
        public static final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_VIDEO_ID + " TEXT UNIQUE NOT NULL, " +
                COLUMN_MOVIE_ID + " LONG REFERENCES " + MovieEntry.TABLE_NAME + " ("+MovieEntry.COLUMN_MOVIE_IMDB_ID + ") ON UPDATE CASCADE," +
                COLUMN_VIDEO_LANGUAGE + " TEXT DEFAULT '', " +
                COLUMN_VIDEO_KEY + " TEXT NOT NULL, " +
                COLUMN_VIDEO_NAME + " TEXT DEFAULT '', " +
                COLUMN_VIDEO_SITE + " TEXT NOT NULL, " +
                COLUMN_VIDEO_TYPE + " TEXT NOT NULL" +
                " );";
        //Cursors
        public static final String[] VIDEO_COLUMNS = {
                MovieContract.VideoEntry._ID,
                MovieContract.VideoEntry.COLUMN_VIDEO_NAME,
                MovieContract.VideoEntry.COLUMN_VIDEO_KEY,
                MovieContract.VideoEntry.COLUMN_VIDEO_SITE,
        };
        public static final int COLUMN_VIDEO_KEY_INDEX = 2;
        public static final int COLUMN_VIDEO_NAME_INDEX = 1;
        public static final int COLUMN_VIDEO_SITE_INDEX = 3;

        //Uri
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEO).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;

        public static Uri buildVideoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
