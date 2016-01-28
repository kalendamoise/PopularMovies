package com.focusandcode.popularmovies.adapters;

import com.focusandcode.popularmovies.utils.Constants;
import com.focusandcode.popularmovies.Entities.ListMovieReviews;
import com.focusandcode.popularmovies.Entities.ListMovieVideos;
import com.focusandcode.popularmovies.Services.MovieService;
import com.google.gson.Gson;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by Moise2022 on 12/5/15.
 */
public class RestClient {

    protected static final String LOG_TAG = RestClient.class.getName();
    protected static RestAdapter restAdapter;
    protected static MovieService movieService;
    protected ListMovieReviews reviews;
    protected ListMovieVideos videos;


    public static MovieService getMovieService () {
        if (movieService == null) {
            restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(Constants.BASE_URL)
                    .setConverter(new GsonConverter(new Gson()))
                    .setErrorHandler(new RetrofitErrorHandler())
                    .build();
            movieService = restAdapter.create(MovieService.class); // create the interface
        }

        return movieService;
    }
}
