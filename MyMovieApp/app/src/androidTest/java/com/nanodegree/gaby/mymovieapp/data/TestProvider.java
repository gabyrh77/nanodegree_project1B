/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nanodegree.gaby.mymovieapp.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.nanodegree.gaby.mymovieapp.data.MovieContract.MovieEntry;
import com.nanodegree.gaby.mymovieapp.data.MovieContract.VideoEntry;
import com.nanodegree.gaby.mymovieapp.data.MovieContract.ReviewEntry;

/*
    Note: This is not a complete set of tests of the ContentProvider, but it does test
    that at least the basic functionality has been implemented correctly.

    Students: Uncomment the tests in this class as you implement the functionality in your
    ContentProvider to make sure that you've implemented things reasonably correctly.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    /*
       This helper function deletes all records from both database tables using the ContentProvider.
       It also queries the ContentProvider to make sure that the database has been successfully
       deleted, so it cannot be used until the Query and Delete functions have been written
       in the ContentProvider.

       Students: Replace the calls to deleteAllRecordsFromDB with this one after you have written
       the delete functionality in the ContentProvider.
     */
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                MovieEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                ReviewEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                VideoEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Movie table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                VideoEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Video table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                ReviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Review table during delete", 0, cursor.getCount());
        cursor.close();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecordsFromProvider();
    }

    /*
        This test checks to make sure that the content provider is registered correctly.
        Students: Uncomment this test to make sure you've correctly registered the WeatherProvider.
     */
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MovieProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: MovieProvider registered with authority: " + providerInfo.authority +
                    " instead of authority: " + MovieContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MovieContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: MovieProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    /*
            This test doesn't touch the database.  It verifies that the ContentProvider returns
            the correct type for each type of URI that it can handle.

         */
    public void testMovieGetType() {
        // content://com.nanodegree.gaby.mymovieapp/movie/
        String type = mContext.getContentResolver().getType(MovieEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.nanodegree.gaby.mymovieapp/movie
        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE",
                MovieEntry.CONTENT_TYPE, type);

        long testId = 94074;
        // content://com.nanodegree.gaby.mymovieapp/movie/94074
        type = mContext.getContentResolver().getType(
                MovieEntry.buildMovieUri(testId));
        // vnd.android.cursor.item/com.nanodegree.gaby.mymovieapp/movie
        assertEquals("Error: the MovieEntry CONTENT_URI with id should return MovieEntry.CONTENT_ITEM",
                MovieEntry.CONTENT_ITEM_TYPE, type);

    }

    public void testMReviewGetType() {
        // content://com.nanodegree.gaby.mymovieapp/review/
        String type = mContext.getContentResolver().getType(ReviewEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.nanodegree.gaby.mymovieapp/review
        assertEquals("Error: the ReviewEntry CONTENT_URI should return ReviewEntry.CONTENT_TYPE",
                ReviewEntry.CONTENT_TYPE, type);

        long testId = 94074;
        // content://com.nanodegree.gaby.mymovieapp/review/94074
        type = mContext.getContentResolver().getType(
                ReviewEntry.buildReviewUri(testId));
        // vnd.android.cursor.item/com.nanodegree.gaby.mymovieapp/review
        assertEquals("Error: the ReviewEntry CONTENT_URI with id should return ReviewEntry.CONTENT_ITEM",
                ReviewEntry.CONTENT_ITEM_TYPE, type);

    }

    public void testVideoGetType() {
        // content://com.nanodegree.gaby.mymovieapp/video/
        String type = mContext.getContentResolver().getType(VideoEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.nanodegree.gaby.mymovieapp/video
        assertEquals("Error: the VideoEntry CONTENT_URI should return VideoEntry.CONTENT_TYPE",
                VideoEntry.CONTENT_TYPE, type);

        long testId = 94074;
        // content://com.nanodegree.gaby.video/movie/94074
        type = mContext.getContentResolver().getType(
                VideoEntry.buildVideoUri(testId));
        // vnd.android.cursor.item/com.nanodegree.gaby.mymovieapp/video
        assertEquals("Error: the VideoEntry CONTENT_URI with id should return VideoEntry.CONTENT_ITEM",
                VideoEntry.CONTENT_ITEM_TYPE, type);

    }


    /*
        This test uses the database directly to insert and then uses the ContentProvider to
        read out the data.  Uncomment this test to see if the basic weather query functionality
        given in the ContentProvider is working correctly.
     */
    public void testBasicMovieQuery() {
        // insert our test records into the database
        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestData.createMovieValues();

        long insertedRowId = db.insert(MovieEntry.TABLE_NAME, null, testValues);
        assertTrue("Unable to Insert MovieEntry into the Database", insertedRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor resultCursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicMovieQuery", resultCursor, testValues);
    }


    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;
    static ContentValues[] createBulkInsertMovieValues() {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++ ) {
            ContentValues cValues = new ContentValues();
            cValues.put(MovieEntry.COLUMN_MOVIE_IMDB_ID, i+1);
            cValues.put(MovieEntry.COLUMN_MOVIE_FAVORITE, 0);
            cValues.put(MovieEntry.COLUMN_MOVIE_SYNOPSIS, "Some synopsis test");
            cValues.put(MovieEntry.COLUMN_MOVIE_RELEASE_DATE, "2015-02-02");
            cValues.put(MovieEntry.COLUMN_MOVIE_POPULARITY, 100);
            cValues.put(MovieEntry.COLUMN_MOVIE_TITLE, "Test Title");
            cValues.put(MovieEntry.COLUMN_MOVIE_RATING, 10-i);
            returnContentValues[i] = cValues;
        }
        return returnContentValues;
    }

    public void testMovieBulkInsert() {

        ContentValues[] bulkInsertContentValues = createBulkInsertMovieValues();

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver observer = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieEntry.CONTENT_URI, true, observer);

        int insertCount = mContext.getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, bulkInsertContentValues);

        // Students:  If this fails, it means that you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
        // ContentProvider method.
        observer.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(observer);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testMovieBulkInsert.  Error validating MovieEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }

    static ContentValues[] createBulkInsertReviewValues() {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++ ) {
            ContentValues cValues = new ContentValues();
            cValues.put(ReviewEntry.COLUMN_MOVIE_ID, TestData.IMDB_MOVIE_ID);
            cValues.put(ReviewEntry.COLUMN_REVIEW_ID, String.valueOf(i+1));
            cValues.put(ReviewEntry.COLUMN_REVIEW_AUTHOR, "SOME AUTHOR");
            cValues.put(ReviewEntry.COLUMN_REVIEW_CONTENT, "Some fake content");
            returnContentValues[i] = cValues;
        }
        return returnContentValues;
    }

    public void testReviewBulkInsert() {

        ContentValues[] bulkInsertContentValues = createBulkInsertReviewValues();

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver observer = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(ReviewEntry.CONTENT_URI, true, observer);

        int insertCount = mContext.getContentResolver().bulkInsert(ReviewEntry.CONTENT_URI, bulkInsertContentValues);

        // Students:  If this fails, it means that you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
        // ContentProvider method.
        observer.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(observer);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                ReviewEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null //sort order
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testReviewBulkInsert.  Error validating ReviewEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }

    static ContentValues[] createBulkInsertVideoValues() {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++ ) {
            ContentValues cValues = new ContentValues();
            cValues.put(VideoEntry.COLUMN_MOVIE_ID, TestData.IMDB_MOVIE_ID);
            cValues.put(VideoEntry.COLUMN_VIDEO_ID, String.valueOf(i+1));
            cValues.put(VideoEntry.COLUMN_VIDEO_KEY, "AJtDXIazrMo");
            cValues.put(VideoEntry.COLUMN_VIDEO_TYPE, "Trailer");
            cValues.put(VideoEntry.COLUMN_VIDEO_SITE, "YouTube");
            returnContentValues[i] = cValues;
        }
        return returnContentValues;
    }

    public void testVideoBulkInsert() {

        ContentValues[] bulkInsertContentValues = createBulkInsertVideoValues();

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver observer = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(VideoEntry.CONTENT_URI, true, observer);

        int insertCount = mContext.getContentResolver().bulkInsert(VideoEntry.CONTENT_URI, bulkInsertContentValues);

        // Students:  If this fails, it means that you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
        // ContentProvider method.
        observer.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(observer);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                VideoEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null //sort order
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testVideoBulkInsert.  Error validating VideoEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }
}
