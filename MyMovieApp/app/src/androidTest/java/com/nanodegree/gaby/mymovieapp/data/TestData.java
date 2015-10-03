package com.nanodegree.gaby.mymovieapp.data;

import android.content.ContentValues;

import com.nanodegree.gaby.mymovieapp.data.MovieContract;

/**
 * Created by Gaby on 9/26/2015.
 */
public class TestData {
    public static long IMDB_MOVIE_ID = 2345;

    static ContentValues createMovieValues() {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMDB_ID, IMDB_MOVIE_ID);
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, "TEST TITLE");
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, "/someFakePath.jpg");
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RATING, 1.2);
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY, 1.3);
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_SYNOPSIS, "Some fake movie synopsis");
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, "1999-05-01");

        return movieValues;
    }
    static ContentValues createReviewValues() {
        ContentValues reviewValues = new ContentValues();
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, IMDB_MOVIE_ID);
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID, "12345");
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHOR, "SOME AUTHOR");
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_URL, "http://someFakePath.jpg");
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT, "Some fake content");

        return reviewValues;
    }

    static ContentValues createReviewValuesNoUrl() {
        ContentValues reviewValues = new ContentValues();
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, IMDB_MOVIE_ID);
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID, "23456");
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHOR, "SOME AUTHOR");
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT, "Some fake content");

        return reviewValues;
    }

    static ContentValues createVideoValuesNoName() {
        ContentValues videoValues = new ContentValues();
        videoValues.put(MovieContract.VideoEntry.COLUMN_MOVIE_ID, IMDB_MOVIE_ID);
        videoValues.put(MovieContract.VideoEntry.COLUMN_VIDEO_ID, "12345");
        videoValues.put(MovieContract.VideoEntry.COLUMN_VIDEO_KEY, "AJtDXIazrMo");
        videoValues.put(MovieContract.VideoEntry.COLUMN_VIDEO_TYPE, "Trailer");
        videoValues.put(MovieContract.VideoEntry.COLUMN_VIDEO_SITE, "YouTube");
        videoValues.put(MovieContract.VideoEntry.COLUMN_VIDEO_LANGUAGE, "en");

        return videoValues;
    }
    static ContentValues createVideoValues() {
        ContentValues videoValues = new ContentValues();
        videoValues.put(MovieContract.VideoEntry.COLUMN_MOVIE_ID, IMDB_MOVIE_ID);
        videoValues.put(MovieContract.VideoEntry.COLUMN_VIDEO_ID, "23456");
        videoValues.put(MovieContract.VideoEntry.COLUMN_VIDEO_KEY, "AJtDXIazrMo");
        videoValues.put(MovieContract.VideoEntry.COLUMN_VIDEO_TYPE, "Trailer");
        videoValues.put(MovieContract.VideoEntry.COLUMN_VIDEO_NAME, "Trailer Movie 1");
        videoValues.put(MovieContract.VideoEntry.COLUMN_VIDEO_SITE, "YouTube");
        videoValues.put(MovieContract.VideoEntry.COLUMN_VIDEO_LANGUAGE, "en");

        return videoValues;
    }
}
