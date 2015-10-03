package com.nanodegree.gaby.mymovieapp.api;

import java.util.List;

/**
 * Created by Gaby on 9/28/2015.
 */
public class MovieVideosResponse {
    private Integer id;
    private List<VideoResponse> results;

    public Integer getId() {
        return id;
    }

    public List<VideoResponse> getResults() {
        return results;
    }
}
