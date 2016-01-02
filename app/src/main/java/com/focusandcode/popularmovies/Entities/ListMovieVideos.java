package com.focusandcode.popularmovies.Entities;

import java.util.List;

/**
 * Created by Moise2022 on 12/6/15.
 */

public class ListMovieVideos {
    private String id;
    private List<MovieVideo> results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MovieVideo> getResults() {
        return results;
    }

    public void setResults(List<MovieVideo> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "ListMovieVideos{" +
                "id='" + id + '\'' +
                ", results=" + results +
                '}';
    }
}