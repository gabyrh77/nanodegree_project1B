package com.nanodegree.gaby.mymovieapp.api;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Gaby on 9/26/2015.
 */
public interface MovieAPI {
    String REVIEWS_PATH = "movie/{id}/reviews";
    String MOVIES_PATH = "discover/movie";
    String VIDEOS_PATH = "movie/{id}/videos";

    @GET(MOVIES_PATH)
    Call<DiscoverMovieResponse> listMoviesWithOrder(@Query("api_key") String api, @Query("sort_by") String sort);

    @GET(REVIEWS_PATH)
    Call<MovieReviewsResponse> listMovieReviews(@Path("id") long movieId, @Query("api_key") String api);

    @GET(VIDEOS_PATH)
    Call<MovieVideosResponse> listMovieVideos(@Path("id") long videoId, @Query("api_key") String api);
}
