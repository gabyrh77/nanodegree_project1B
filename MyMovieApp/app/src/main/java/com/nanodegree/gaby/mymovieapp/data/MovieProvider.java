package com.nanodegree.gaby.mymovieapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Gaby on 9/26/2015.
 */
public class MovieProvider extends ContentProvider {
    private static UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper mOpenHelper;
    static final int MOVIES = 100;
    static final int MOVIE = 101;
    static final int REVIEWS = 200;
    static final int REVIEW = 201;
    static final int VIDEOS = 300;
    static final int VIDEO = 301;

    static UriMatcher buildUriMatcher() {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE + "/" , MOVIES);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE + "/#", MOVIE);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_REVIEW + "/" , REVIEWS);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_REVIEW + "/*", REVIEW);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_VIDEO + "/" , VIDEOS);
        sUriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_VIDEO+ "/*", VIDEO);
        return sUriMatcher;
    }

    private static final SQLiteQueryBuilder sMovieQueryBuilder;
    private static final SQLiteQueryBuilder sReviewQueryBuilder;
    private static final SQLiteQueryBuilder sVideoQueryBuilder;

    static{
        sMovieQueryBuilder = new SQLiteQueryBuilder();
        sMovieQueryBuilder.setTables(
                MovieContract.MovieEntry.TABLE_NAME);
        sReviewQueryBuilder = new SQLiteQueryBuilder();
        sReviewQueryBuilder.setTables(
                MovieContract.ReviewEntry.TABLE_NAME);
        sVideoQueryBuilder = new SQLiteQueryBuilder();
        sVideoQueryBuilder.setTables(
                MovieContract.VideoEntry.TABLE_NAME);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            case MOVIES:
            case MOVIE:
                retCursor = sMovieQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case REVIEWS:
            case REVIEW:
                retCursor = sReviewQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case VIDEOS:
            case VIDEO:
                retCursor = sVideoQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (retCursor != null) {
            retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return  MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case REVIEWS:
                return  MovieContract.ReviewEntry.CONTENT_TYPE;
            case REVIEW:
                return MovieContract.ReviewEntry.CONTENT_ITEM_TYPE;
            case VIDEOS:
                return  MovieContract.VideoEntry.CONTENT_TYPE;
            case VIDEO:
                return MovieContract.VideoEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE: {
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEW: {
                long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, contentValues);
                if ( _id > 0 )
                    returnUri = MovieContract.ReviewEntry.buildReviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case VIDEO: {
                long _id = db.insert(MovieContract.VideoEntry.TABLE_NAME, null, contentValues);
                if ( _id > 0 )
                    returnUri = MovieContract.VideoEntry.buildVideoUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        String tableName = null, idColumn = null;

        //determinate table
        switch (match) {
            case MOVIES:
                tableName = MovieContract.MovieEntry.TABLE_NAME;
                idColumn = MovieContract.MovieEntry.COLUMN_MOVIE_IMDB_ID;
                break;
            case REVIEWS:
                tableName = MovieContract.ReviewEntry.TABLE_NAME;
                idColumn = MovieContract.ReviewEntry.COLUMN_REVIEW_ID;
                break;
            case VIDEOS:
                tableName = MovieContract.VideoEntry.TABLE_NAME;
                idColumn = MovieContract.VideoEntry.COLUMN_VIDEO_ID;
                break;
        }

        //perform bulk insert
        if(tableName!=null){
            db.beginTransaction();
            int returnCount = 0;
            try {
                for (ContentValues value : values) {

                    long _id = db.insert(tableName, null, value);
                    if (_id != -1) {
                        returnCount++;
                    }else{
                        if(value.containsKey(idColumn)) {
                            String where = idColumn + " = ?";
                            String[] args = new String[]{String.valueOf(value.get(idColumn))};
                            value.remove(idColumn);
                            long _row = db.update(tableName, value, where, args);
                            if (_row>0){
                                returnCount++;
                            }
                        }
                    }
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return returnCount;
        }else{
            return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case MOVIES:
            case MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REVIEWS:
            case REVIEW:
                rowsDeleted = db.delete(
                        MovieContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case VIDEOS:
            case VIDEO:
                rowsDeleted = db.delete(
                        MovieContract.VideoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE:
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            case REVIEW:
                rowsUpdated = db.update(MovieContract.ReviewEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            case VIDEO:
                rowsUpdated = db.update(MovieContract.VideoEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
