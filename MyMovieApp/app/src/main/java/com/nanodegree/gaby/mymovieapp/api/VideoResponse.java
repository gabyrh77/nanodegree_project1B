package com.nanodegree.gaby.mymovieapp.api;

/**
 * Created by Gaby on 9/28/2015.
 */
public class VideoResponse {
    private String id;
    private String iso_639_1;
    private String key;
    private String name;
    private String site;
    private Integer size;
    private String type;

    public String getId() {
        return id;
    }

    public String getLanguage() {
        return iso_639_1;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public Integer getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}
