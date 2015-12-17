package com.focusandcode.popularmovies;

import android.os.AsyncTask;

import com.focusandcode.popularmovies.Entities.ListMovieVideos;
import com.focusandcode.popularmovies.Services.MovieService;
import com.focusandcode.popularmovies.adapters.RestClient;

/**
 * Created by Moise2022 on 12/15/15.
 */
public class FetchVideosTask extends AsyncTask<String, Void, ListMovieVideos> {
    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private ListMovieVideos movieVideos;

    FetchVideosTask(ListMovieVideos movieVideos) {
        this.movieVideos = movieVideos;
    }


    @Override
    protected ListMovieVideos doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        MovieService movieService = RestClient.getMovieService();
        ListMovieVideos movieVideos = movieService.getMovieVideos(params[0], params[1]);

        if (movieVideos == null) {
            return null;
        }
        return movieVideos;

    }

    @Override
    protected void onPostExecute(ListMovieVideos results) {
        if (results != null) {
            this.movieVideos = results;
        }
    }

    @Override
    protected void onPreExecute() {

    }
}