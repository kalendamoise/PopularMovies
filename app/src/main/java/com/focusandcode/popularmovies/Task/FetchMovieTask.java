package com.focusandcode.popularmovies.Task;


import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.focusandcode.popularmovies.Entities.ListMovies;
import com.focusandcode.popularmovies.Entities.Movie;
import com.focusandcode.popularmovies.Services.MovieService;
import com.focusandcode.popularmovies.adapters.GridViewAdapter;
import com.focusandcode.popularmovies.adapters.RestClient;

import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by Moise2022 on 9/23/15.
 */
public class FetchMovieTask  extends AsyncTask<String, Void, List<Movie>> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private GridViewAdapter adapter;
    private LinearLayout spinnerView;

    public FetchMovieTask(GridViewAdapter adapter, LinearLayout spinerView) {
        this.adapter = adapter;
        this.spinnerView = spinerView;

    }

    @Override
    protected List<Movie> doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }
        MovieService movieService = RestClient.getMovieService();

        ListMovies listMovies = null;
        try {
            listMovies = movieService.getMovies(params[0], params[1], params[2]);
        }
        catch(RetrofitError error){
            methodToDeterminErrorType(error);
        }

        if (listMovies == null) {
            return  null;
        }
        return listMovies.getResults();

    }

    private void methodToDeterminErrorType(RetrofitError error){
        switch (error.getKind()) {
            case CONVERSION:
                Log.e(LOG_TAG, error.getMessage());
                break;
            case HTTP:
                Log.e(LOG_TAG, error.getMessage());
                break;
            case NETWORK:
                Log.e(LOG_TAG, error.getMessage());
                break;
            case UNEXPECTED:
                Log.e(LOG_TAG, error.getMessage());
        }
    }

    @Override
    protected void onPostExecute(List<Movie> results) {
        if (spinnerView != null) {
            spinnerView.setVisibility(View.GONE);
        }
        if (adapter.getMovies() != null && results != null) {
            adapter.getMovies().addAll(results);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPreExecute() {
        if(spinnerView != null) {
            spinnerView.setVisibility(View.VISIBLE);
        }
    }
}