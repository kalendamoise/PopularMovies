package com.focusandcode.popularmovies.adapters;

import android.util.Log;

import retrofit.RetrofitError;
import retrofit.ErrorHandler;
import retrofit.client.Response;

/**
 * Created by Moise2022 on 12/5/15.
 */
public class RetrofitErrorHandler implements ErrorHandler {
    protected final String LOG_TAG = RetrofitErrorHandler.class.getName();
    @Override
    public Throwable handleError(RetrofitError cause) {
        Response r = cause.getResponse();
        if (r != null && r.getStatus() == 401) {
            Log.e(LOG_TAG, "Error:", cause);
        }
        return cause;

    }

}
