package com.focusandcode.popularmovies;


import android.os.AsyncTask;
import android.util.Log;

import com.focusandcode.popularmovies.Entities.ListMovieReviews;
import com.focusandcode.popularmovies.Services.MovieService;
import com.focusandcode.popularmovies.adapters.MovieReviewAdapter;
import com.focusandcode.popularmovies.adapters.RestClient;

/**
 * Created by Moise2022 on 12/15/15.
 */
public class FetchReviewsTask extends AsyncTask<String, Void, ListMovieReviews> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private MovieReviewAdapter adapter;

    FetchReviewsTask(MovieReviewAdapter adapter) {
        this.adapter = adapter;
    }


    @Override
    protected ListMovieReviews doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }
        MovieService service = RestClient.getMovieService();
        return service.getMovieReviews(params[0], params[1]);

    }

    @Override
    protected void onPostExecute(ListMovieReviews results) {
        if (results != null) {
            this.adapter.addAll(results.getResults());
            Log.d(LOG_TAG, "Videos: " + results.getResults());
        }
    }

    @Override
    protected void onPreExecute() {

    }
}