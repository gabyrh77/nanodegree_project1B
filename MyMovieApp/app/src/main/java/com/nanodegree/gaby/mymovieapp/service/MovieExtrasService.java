package com.nanodegree.gaby.mymovieapp.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.nanodegree.gaby.mymovieapp.api.MovieAPIService;
import com.nanodegree.gaby.mymovieapp.api.MovieReviewsResponse;
import com.nanodegree.gaby.mymovieapp.api.MovieVideosResponse;
import com.nanodegree.gaby.mymovieapp.api.ReviewResponse;
import com.nanodegree.gaby.mymovieapp.api.VideoResponse;
import com.nanodegree.gaby.mymovieapp.data.MovieContract;

import java.util.List;
import java.util.Vector;

/**
 * Created by Gaby on 9/28/2015.
 */
public class MovieExtrasService extends IntentService{
    public static final String MOVIE_ID_EXTRA = "movie_id";
    public static final String LOG_TAG = MovieExtrasService.class.getSimpleName();
    public static final String BROADCAST_ACTION_COMPLETE =
            "com.nanodegree.gaby.mymovieapp.service.BROADCAST_ACTION_COMPLETE";
    public MovieExtrasService(){
        super("MovieExtrasService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long movieId = intent.getLongExtra(MOVIE_ID_EXTRA, 0);
        if(movieId>0){
            MovieAPIService apiService = new MovieAPIService();
            //fetch videos and insert to DB
            MovieVideosResponse videosResponse = apiService.fetchMovieVideos(movieId);
            if(videosResponse!=null && videosResponse.getResults()!=null){
                bulkVideoInsert(getContentResolver(), videosResponse.getResults(), movieId);
            }
            //fetch reviews and insert to DB
            MovieReviewsResponse reviewsResponse = apiService.fetchMovieReviews(movieId);
            if(reviewsResponse!=null && reviewsResponse.getResults()!=null){
                bulkReviewInsert(getContentResolver(), reviewsResponse.getResults(), movieId);
            }
        }
        Intent localIntent = new Intent(BROADCAST_ACTION_COMPLETE);
        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }



    private void bulkVideoInsert(ContentResolver resolver, List<VideoResponse> videos, long movieId){
        // Initialize the content values vector
        Vector<ContentValues> contentValuesVector = new Vector<>(videos.size());

        for (VideoResponse video:videos){
            //is the data ok?
            if(video!=null && video.getId()!= null && video.getKey()!=null && video.getSite()!=null && video.getType()!=null) {
                //store movie data on content value
                ContentValues cValues = new ContentValues();
                cValues.put(MovieContract.VideoEntry.COLUMN_VIDEO_ID, video.getId());
                cValues.put(MovieContract.VideoEntry.COLUMN_VIDEO_NAME, video.getName());
                cValues.put(MovieContract.VideoEntry.COLUMN_VIDEO_LANGUAGE, video.getLanguage());
                cValues.put(MovieContract.VideoEntry.COLUMN_VIDEO_SITE, video.getSite());
                cValues.put(MovieContract.VideoEntry.COLUMN_VIDEO_TYPE, video.getType());
                cValues.put(MovieContract.VideoEntry.COLUMN_VIDEO_KEY, video.getKey());
                cValues.put(MovieContract.VideoEntry.COLUMN_MOVIE_ID, movieId);

                //add to vector
                contentValuesVector.add(cValues);
            }
        }

        // perform bulk insert
        if ( contentValuesVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[contentValuesVector.size()];
            contentValuesVector.toArray(cvArray);
            resolver.bulkInsert(MovieContract.VideoEntry.CONTENT_URI, cvArray);
        }
    }

    private void bulkReviewInsert(ContentResolver resolver, List<ReviewResponse> reviews, long movieId){
        // Initialize the content values vector
        Vector<ContentValues> contentValuesVector = new Vector<>(reviews.size());

        for (ReviewResponse review:reviews){
            //is the data ok?
            if(review!=null && review.getId()!=null && review.getContent()!=null) {
                //store movie data on content value
                ContentValues cValues = new ContentValues();
                cValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID, review.getId());
                cValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHOR, review.getAuthor());
                cValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_CONTENT, review.getContent());
                cValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_URL, review.getUrl());
                cValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, movieId);

                //add to vector
                contentValuesVector.add(cValues);
            }
        }

        // perform bulk insert
        if ( contentValuesVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[contentValuesVector.size()];
            contentValuesVector.toArray(cvArray);
            resolver.bulkInsert(MovieContract.ReviewEntry.CONTENT_URI, cvArray);
        }
    }

    public static class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent sendIntent = new Intent(context, MovieExtrasService.class);
            sendIntent.putExtra(MovieExtrasService.MOVIE_ID_EXTRA, intent.getLongExtra(MovieExtrasService.MOVIE_ID_EXTRA, 0));
            context.startService(sendIntent);

        }
    }
}
