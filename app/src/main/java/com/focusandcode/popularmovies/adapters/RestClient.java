package com.focusandcode.popularmovies.adapters;

import android.util.Log;

import com.focusandcode.popularmovies.Constants;
import com.focusandcode.popularmovies.Entities.ListMovieReviews;
import com.focusandcode.popularmovies.Entities.ListMovieVideos;
import com.focusandcode.popularmovies.Services.MovieService;
import com.google.gson.Gson;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by Moise2022 on 12/5/15.
 */
public class RestClient {

    protected final String LOG_TAG = RestClient.class.getName();
    protected RestAdapter restAdapter;
    protected MovieService movieService;
    protected GsonConverter converter;
    protected ListMovieReviews reviews;
    protected ListMovieVideos videos;


    public MovieService getMovieService () {
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

    public void getMovieReviews(Long id , Callback<ListMovieReviews> callback){
        Log.d(LOG_TAG, "Get movie reviews for id: " + id);
        movieService.getMovieReviews(id, Constants.API_KEY, callback);
    }


    public void getMovieVideos(Long id , Callback<ListMovieVideos> callback){
        Log.d(LOG_TAG, "Get movie videos for id: " + id);
        movieService.getMovieVideos(id, Constants.API_KEY, callback);
    }


    private Callback<ListMovieReviews> movieReviews = new Callback<ListMovieReviews>() {
        @Override
        public void success(ListMovieReviews data, Response response) {
            Log.d(LOG_TAG, "Reviews: " + data.toString());
            // do other thing
            reviews = data;
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d(LOG_TAG,"failure: " + error);
            // do other thing
        }
    };

    private Callback<ListMovieVideos> movieVideos = new Callback<ListMovieVideos>() {
        @Override
        public void success(ListMovieVideos data, Response response) {
            Log.d(LOG_TAG, "Videos: " + data.toString());
            // do other thing
            videos = data;
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d(LOG_TAG,"failure: " + error);
            // do other thing
        }
    };

    public void runRetrofitTestSync (final Long id) {


        if (movieService == null)
            movieService = getMovieService();

        // Test Sync version -- in a separate thread
        // Not doing this will crash the app. As Retro sync calls can not be made from
        // UI thread.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Call Async API -- always call in a try block if you dont want app to
                    // crash. You get 'HTTP/1.1 500 Internal Server Error' more often than
                    // you think.
                    getMovieReviews(id, movieReviews);
                    getMovieVideos(id, movieVideos);

                    //Log.d(LOG_TAG, reviews.toString());

                } catch (Exception ex) {
                    Log.e(LOG_TAG, "Sync: exception", ex);
                } finally {

                }
            }
        }).start();
    }
}
