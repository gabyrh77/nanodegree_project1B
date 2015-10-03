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

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieContract.MovieEntry.TABLE_NAME);

        mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDBHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without movie entry table",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> columnHashSet = new HashSet<String>();
        columnHashSet.add(MovieContract.MovieEntry._ID);
        columnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_IMDB_ID);
        columnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
        columnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER);
        columnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_RATING);
        columnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY);
        columnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS);
        columnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE);
        columnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            columnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required movie
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required movie entry columns",
                columnHashSet.isEmpty());
        db.close();
    }

    /*
        Here is tested that we can insert and query the
        database.
     */
    public void testMovieTable() {

        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create movie values
        ContentValues movieValues = TestData.createMovieValues();

        // Third Step: Insert ContentValues into database and get a row ID back
        long insertedRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, movieValues);
        assertTrue(insertedRowId != -1);

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor resultCursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue( "Error: No Records returned from movie query", resultCursor.moveToFirst() );

        // Fifth Step: Validate the Query
        TestUtilities.validateCurrentRecord("testInsertReadDb MovieEntry failed to validate",
                resultCursor, movieValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from movie query",
                resultCursor.moveToNext() );

        // Sixth Step: Close cursor and database
        resultCursor.close();
        dbHelper.close();
    }

    /*
       Here is tested that we can insert and query the
       database.
    */
    public void testReviewTable() {

        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create review values
        ContentValues movieValues = TestData.createReviewValues();

        // Third Step: Insert ContentValues into database and get a row ID back
        long insertedRowId = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, movieValues);
        assertTrue(insertedRowId != -1);

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor resultCursor = db.query(
                MovieContract.ReviewEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue("Error: No Records returned from review query", resultCursor.moveToFirst());

        // Fifth Step: Validate the Query
        TestUtilities.validateCurrentRecord("testInsertReadDb ReviewEntry failed to validate",
                resultCursor, movieValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from review query",
                resultCursor.moveToNext());

        // Sixth Step: Close cursor and database
        resultCursor.close();

        // 7th Step: Create review values no url
        movieValues = TestData.createReviewValuesNoUrl();

        // 8th Step: Insert ContentValues into database and get a row ID back
        long insertedRowId2 = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, movieValues);
        assertTrue(insertedRowId2 != -1);

        // 9th Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        resultCursor = db.query(
                MovieContract.ReviewEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                MovieContract.ReviewEntry._ID + " = ?", // cols for "where" clause
                new String[]{String.valueOf(insertedRowId2)}, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue("Error: No Records returned from review query", resultCursor.moveToFirst());

        // 10th Step: Validate the Query
        TestUtilities.validateCurrentRecord("testInsertReadDb ReviewEntry failed to validate",
                resultCursor, movieValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from review query",
                resultCursor.moveToNext());

        // 11th Step: Close cursor and database
        resultCursor.close();
        dbHelper.close();
    }

    /*
      Here is tested that we can insert and query the
      database.
   */
    public void testVideoTable() {

        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create video values
        ContentValues movieValues = TestData.createVideoValues();

        // Third Step: Insert ContentValues into database and get a row ID back
        long insertedRowId = db.insert(MovieContract.VideoEntry.TABLE_NAME, null, movieValues);
        assertTrue(insertedRowId != -1);

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor resultCursor = db.query(
                MovieContract.VideoEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue("Error: No Records returned from video query", resultCursor.moveToFirst());

        // Fifth Step: Validate the Query
        TestUtilities.validateCurrentRecord("testInsertReadDb VideoEntry failed to validate",
                resultCursor, movieValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from video query",
                resultCursor.moveToNext() );
        // Sixth Step: Close cursor and database
        resultCursor.close();

        // 7th Step: Create video values no name
        movieValues = TestData.createVideoValuesNoName();

        // 8th Step: Insert ContentValues into database and get a row ID back
        long insertedRowId2 = db.insert(MovieContract.VideoEntry.TABLE_NAME, null, movieValues);
        assertTrue(insertedRowId2 != -1);

        // 9th Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        resultCursor = db.query(
                MovieContract.VideoEntry.TABLE_NAME,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                MovieContract.VideoEntry._ID + " = ?", // cols for "where" clause
                new String[]{String.valueOf(insertedRowId2)}, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue("Error: No Records returned from video query", resultCursor.moveToFirst());

        // 10th Step: Validate the Query
        TestUtilities.validateCurrentRecord("testInsertReadDb VideoEntry failed to validate",
                resultCursor, movieValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse("Error: More than one record returned from vieo query",
                resultCursor.moveToNext());

        // Sixth Step: Close cursor and database
        resultCursor.close();
        dbHelper.close();
    }
}
