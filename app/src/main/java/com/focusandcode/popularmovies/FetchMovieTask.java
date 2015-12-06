package com.focusandcode.popularmovies;


import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.focusandcode.popularmovies.Entities.Movie;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moise2022 on 9/23/15.
 */
public class FetchMovieTask  extends AsyncTask<String, Void, List<Movie>> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private GridViewAdapter adapter;
    private LinearLayout spinnerView;

    FetchMovieTask(GridViewAdapter adapter, LinearLayout spinerView) {
        this.adapter = adapter;
        this.spinnerView = spinerView;

    }

    private List<Movie> getMovieDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException {
        //Log.d(LOG_TAG, "Json String: " + forecastJsonStr);
        final String RESULTS = "results";

        JSONObject moviesJson = new JSONObject(forecastJsonStr);
        JSONArray movieArray = moviesJson.getJSONArray(RESULTS);

        List<Movie> movies = new ArrayList<Movie>(movieArray.length());
        Gson gson = new Gson();
        for(int i = 0; i < movieArray.length(); i++) {
            JSONObject movieJson = movieArray.getJSONObject(i);
            movies.add(gson.fromJson(movieJson.toString(), Movie.class));
            //Log.d(LOG_TAG, "Movie to String: " + movies.get(i).toString());
        }
        return movies;

    }
    @Override
    protected List<Movie> doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;


        String moviesJsonStr = null;

        String format = "json";

        int limit = 7;

        try {

            Uri builtUri = Uri.parse(Constants.MOVIE_DB_BASE_URL).buildUpon()
                    .appendQueryParameter(Constants.SORT_ORDER_PARAM, params[0])
                    .appendQueryParameter(Constants.API_KEY_PARAM, params[1])
                    .appendQueryParameter(Constants.PAGE, params[2])
                    .build();

            URL url = new URL(builtUri.toString());

            Log.d(LOG_TAG, "URL is: " + url.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            moviesJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMovieDataFromJson(moviesJsonStr, limit);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> results) {
        if (spinnerView != null) {
            spinnerView.setVisibility(View.GONE);
        }
        if (results != null) {
            for(Movie movie : results) {
                adapter.add(movie);
            }
        }
    }

    @Override
    protected void onPreExecute() {
        if(spinnerView != null) {
            spinnerView.setVisibility(View.VISIBLE);
        }
    }
}