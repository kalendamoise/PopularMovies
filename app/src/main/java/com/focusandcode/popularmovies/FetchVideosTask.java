package com.focusandcode.popularmovies;

import android.os.AsyncTask;
import android.util.Log;

import com.focusandcode.popularmovies.Entities.ListMovieVideos;
import com.focusandcode.popularmovies.Services.MovieService;
import com.focusandcode.popularmovies.adapters.MovieVideoAdapter;
import com.focusandcode.popularmovies.adapters.RestClient;

/**
 * Created by Moise2022 on 12/15/15.
 */
public class FetchVideosTask extends AsyncTask<String, Void, ListMovieVideos> {
    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private MovieVideoAdapter adapter;

    FetchVideosTask(MovieVideoAdapter adapter) {
        this.adapter = adapter;
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
            this.adapter.addAll(results.getResults());
            Log.d(LOG_TAG, "Videos: " + results.getResults());
        }
    }

    @Override
    protected void onPreExecute() {

    }
}