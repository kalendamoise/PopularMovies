package com.focusandcode.popularmovies.utils;

import com.focusandcode.popularmovies.BuildConfig;

/**
 * Created by Moise2022 on 9/24/15.
 */
public class Constants {
    public static final String API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE = "w185";
    public static final String NET_STATUS_NOT_CONNECTED = "Not connected to Internet";
    public static final String SEPARATOR = "/";

    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";

    public static final String SORT_ORDER_PARAM = "sort_by";
    public static  final String API_KEY_PARAM = "api_key";
    public static final String PAGE = "page";
    public static final String MOVIE_KEY = "movies";
}
