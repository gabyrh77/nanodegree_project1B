package com.nanodegree.gaby.mymovieapp.api;

import android.util.Log;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;

/**
 * Created by Gaby on 9/27/2015.
 */
public class MovieAPIService {
    private static final String API_URL = "http://api.themoviedb.org/3/";
    private static final String API_KEY = "";
    public static final String POPULARITY_QUERY_PARAM = "popularity.desc";
    public static final String RATING_QUERY_PARAM = "vote_average.desc";
    public static final String YOUTUBE_VIDEO_TYPE = "YouTube";
    public static final String LOG_TAG = MovieAPIService.class.getSimpleName();
    private Retrofit mRetrofit;

    public MovieAPIService(){
        mRetrofit = new Retrofit.Builder().baseUrl(MovieAPIService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


    }

    public DiscoverMovieResponse fetchMoviesWithOrder(String orderQuery){
        try {
            MovieAPI mApi = mRetrofit.create(MovieAPI.class);
            Call<DiscoverMovieResponse> callApi = mApi.listMoviesWithOrder(MovieAPIService.API_KEY, orderQuery);
            Response<DiscoverMovieResponse> movieResponse = callApi.execute();
            if (movieResponse != null && movieResponse.isSuccess()) {
                    return movieResponse.body();
            }else{
                if(movieResponse!=null && movieResponse.errorBody()!=null){
                    Log.e(LOG_TAG, movieResponse.errorBody().string());
                }
            }

        }catch (Exception e){
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }

    public MovieReviewsResponse fetchMovieReviews(long movieId){
        try {
            MovieAPI mApi = mRetrofit.create(MovieAPI.class);
            Call<MovieReviewsResponse> callApi = mApi.listMovieReviews(movieId, MovieAPIService.API_KEY);
            Response<MovieReviewsResponse> movieResponse = callApi.execute();
            if (movieResponse != null && movieResponse.isSuccess()) {
                return movieResponse.body();
            }else{
                if(movieResponse!=null && movieResponse.errorBody()!=null){
                    Log.e(LOG_TAG, movieResponse.errorBody().string());
                }
            }

        }catch (Exception e){
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }

    public MovieVideosResponse fetchMovieVideos(long movieId){
        try {
            MovieAPI mApi = mRetrofit.create(MovieAPI.class);
            Call<MovieVideosResponse> callApi = mApi.listMovieVideos(movieId, MovieAPIService.API_KEY);
            Response<MovieVideosResponse> movieResponse = callApi.execute();
            if (movieResponse != null && movieResponse.isSuccess()) {
                return movieResponse.body();
            }else{
                if(movieResponse!=null && movieResponse.errorBody()!=null){
                    Log.e(LOG_TAG, movieResponse.errorBody().string());
                }
            }

        }catch (Exception e){
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }
}
