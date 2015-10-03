package com.nanodegree.gaby.mymovieapp.api;

import java.util.List;

/**
 * Created by Gaby on 9/28/2015.
 */
public class MovieReviewsResponse {
    private Integer id;
    private Integer page;
    private List<ReviewResponse> results;

    public Integer getId() {
        return id;
    }

    public Integer getPage() {
        return page;
    }

    public List<ReviewResponse> getResults() {
        return results;
    }
}
