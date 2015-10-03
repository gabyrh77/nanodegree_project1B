package com.nanodegree.gaby.mymovieapp.api;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gaby on 9/26/2015.
 */
public class MovieResponse{
    private Long id;
    private String original_title;
    private String overview;
    private String release_date;
    private String poster_path;
    private Float vote_average;
    private Float popularity;

    public Long getId() {
        return id;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public Float getVoteAverage() {
        return vote_average;
    }

    public Float getPopularity() {
        return popularity;
    }
}
