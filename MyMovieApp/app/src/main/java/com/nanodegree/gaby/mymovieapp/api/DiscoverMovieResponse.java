package com.nanodegree.gaby.mymovieapp.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gaby on 9/26/2015.
 */
public class DiscoverMovieResponse {
    private Integer page;
    private List<MovieResponse> results;

    public Integer getPage() {
        return page;
    }

    public List<MovieResponse> getResults() {
        return results;
    }
}
