package com.focusandcode.popularmovies.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Moise2022 on 12/6/15.
 */
public class ListMovieReviews {

    private String id;
    private String page;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("total_results")
    private String totalResults;
    private List<MovieReviews> results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public List<MovieReviews> getResults() {
        return results;
    }

    public void setResults(List<MovieReviews> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "ListMovieReviews{" +
                "id='" + id + '\'' +
                ", page='" + page + '\'' +
                ", totalPages=" + totalPages +
                ", totalResults='" + totalResults + '\'' +
                ", results=" + results +
                '}';
    }
}
