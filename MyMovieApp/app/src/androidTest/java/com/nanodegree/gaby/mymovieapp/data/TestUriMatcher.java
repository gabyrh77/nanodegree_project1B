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

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/*
    Uncomment this class when you are ready to test your UriMatcher.  Note that this class utilizes
    constants that are declared with package protection inside of the UriMatcher, which is why
    the test must be in the same data package as the Android app code.  Doing the test this way is
    a nice compromise between data hiding and testability.
 */
public class TestUriMatcher extends AndroidTestCase {

    // content://com.nanodegree.gaby.mymovieapp/movie
    private static final Uri TEST_MOVIE_DIR = MovieContract.MovieEntry.CONTENT_URI;

    // content://com.nanodegree.gaby.mymovieapp/video
    private static final Uri TEST_VIDEO_DIR = MovieContract.VideoEntry.CONTENT_URI;

    // content://com.nanodegree.gaby.mymovieapp/review
    private static final Uri TEST_REVIEW_DIR = MovieContract.ReviewEntry.CONTENT_URI;

    /*
        Students: This function tests that your UriMatcher returns the correct integer value
        for each of the Uri types that our ContentProvider can handle.  Uncomment this when you are
        ready to test your UriMatcher.
     */
    public void testUriMatcher() {
        UriMatcher testMatcher = MovieProvider.buildUriMatcher();

        assertEquals("Error: The Movie URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIE_DIR), MovieProvider.MOVIES);

        assertEquals("Error: The Video URI was matched incorrectly.",
                testMatcher.match(TEST_VIDEO_DIR), MovieProvider.VIDEOS);

        assertEquals("Error: The Review URI was matched incorrectly.",
                testMatcher.match(TEST_REVIEW_DIR), MovieProvider.REVIEWS);
    }
}
