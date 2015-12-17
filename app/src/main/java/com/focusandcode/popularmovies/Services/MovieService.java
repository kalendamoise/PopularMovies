package com.focusandcode.popularmovies.Services;

import com.focusandcode.popularmovies.Constants;
import com.focusandcode.popularmovies.Entities.ListMovieReviews;
import com.focusandcode.popularmovies.Entities.ListMovieVideos;
import com.focusandcode.popularmovies.Entities.ListMovies;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Moise2022 on 12/3/15.
 */
public interface MovieService {
    @GET("/discover/movie")
    ListMovies getMovies(@Query(Constants.SORT_ORDER_PARAM) String sortBy, @Query(Constants.API_KEY_PARAM) String api_key, @Query(Constants.PAGE) String page);

    @GET("/movie/{id}/videos")
    ListMovieVideos getMovieVideos(@Path("id") String id, @Query(Constants.API_KEY_PARAM) String api_key /*, Callback<ListMovieVideos> callback*/);

    @GET("/movie/{id}/reviews")
    ListMovieReviews getMovieReviews(@Path("id") String id, @Query(Constants.API_KEY_PARAM) String api_key /*, Callback<ListMovieReviews> callback*/);
}
