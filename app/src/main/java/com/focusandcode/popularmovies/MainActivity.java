package com.focusandcode.popularmovies;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.focusandcode.popularmovies.Data.MoviesContract;
import com.focusandcode.popularmovies.Entities.Movie;
import com.focusandcode.popularmovies.Task.FetchMovieTask;
import com.focusandcode.popularmovies.adapters.GridViewAdapter;
import com.focusandcode.popularmovies.utils.Constants;
import com.focusandcode.popularmovies.utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private boolean twoPane;

    private GridViewAdapter adapter;
    GridLayoutManager mLayoutManager;


    private List<Movie> movies = new ArrayList<Movie>();
    private Parcelable state;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.item_list)
    RecyclerView recyclerView;


    private NetworkChangeReceiver broadcastReceiver = new NetworkChangeReceiver();


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null)
        {
            movies = (List<Movie>)savedInstanceState.get(Constants.MOVIE_KEY);
        }

        setSupportActionBar(toolbar);




        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true;
            Log.d(LOG_TAG, "the twoPane is " + twoPane);
        }

        assert recyclerView != null;
        setupRecyclerView(recyclerView);


        if (state != null) {
            Log.d(LOG_TAG, "trying to restore listview state..");
            mLayoutManager.onRestoreInstanceState(state);

        }


        fetchData();

        // Stetho is a tool created by facebook to view your database in chrome inspect.
        // The code below integrates Stetho into your app. More information here:
        // http://facebook.github.io/stetho/

//        Stetho.initialize(
//                Stetho.newInitializerBuilder(this)
//                        .enableDumpapp(
//                                Stetho.defaultDumperPluginsProvider(this))
//                        .enableWebKitInspector(
//                                Stetho.defaultInspectorModulesProvider(this))
//                        .build());


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Constants.MOVIE_KEY, (ArrayList<? extends Parcelable>) movies);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new GridViewAdapter(this, new ArrayList<Movie>(), twoPane, this.getSupportFragmentManager());
        recyclerView.setAdapter(adapter);


        EndlessScrollListener endlessScrollListener = new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {

                fetchDataAppend(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true;
            }
        };

        recyclerView.addOnScrollListener(endlessScrollListener);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void fetchData() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        String sortOrder = preferences.getString(this.getString(R.string.pref_title_sort_order_key),
                this.getString(R.string.pref_sort_order_default));

        Log.d(LOG_TAG, "SortOrder: " + sortOrder);

        if (adapter == null) {
            Log.d(LOG_TAG, "The adapter is null, creating a new one");
            adapter = new GridViewAdapter(this, movies, twoPane, getSupportFragmentManager());
            recyclerView.setAdapter(adapter);
        }

        adapter.getMovies().clear();

        if (sortOrder.equals("favorite")) {
            setTitle("Favorite Movies");
            String orderBy = MoviesContract.MovieEntry.COLUMN_POPULARITY + " DESC";
            Cursor cursor = getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI, null, null, null, orderBy, null);
            Log.d(LOG_TAG, "The number of favorite movies is: " + cursor.getCount());
            while (cursor.moveToNext()) {
                Movie movie = new Movie();
                movie.setTitle(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_TITLE)));
                movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH)));
                movie.setId(cursor.getInt(cursor.getColumnIndex(MoviesContract.MovieEntry._ID)));
                movie.setIsAdult((cursor.getInt(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_ADULT)) != 0));
                movie.setIsVideo((cursor.getInt(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_VIDEO)) != 0));
                movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE)));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_OVERVIEW)));
                movie.setPopularity(cursor.getDouble(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POPULARITY)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_POSTER_PATH)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE)));
                movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE)));
                movie.setVoteCount(cursor.getInt(cursor.getColumnIndex(MoviesContract.MovieEntry.COLUMN_VOTE_COUNT)));
                adapter.getMovies().add(movie);
                Log.d(LOG_TAG, movie.toString());
            }

        }
        else {
            FetchMovieTask task = new FetchMovieTask(adapter, null);
            task.execute(sortOrder, Constants.API_KEY, String.valueOf(1));
        }
        adapter.notifyDataSetChanged();

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void fetchDataAppend(int page) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String sortOrder = preferences.getString(this.getString(R.string.pref_title_sort_order_key),
                this.getString(R.string.pref_sort_order_default));

        Log.d(LOG_TAG, "SortOrder: " + sortOrder);

        if (adapter == null) {
            adapter = new GridViewAdapter(this, movies, twoPane, getSupportFragmentManager());
            recyclerView.setAdapter(adapter);
        }

        if (sortOrder.equals("favorite")) {
            //spinnerView.setVisibility(View.INVISIBLE);
        }else {
            FetchMovieTask task = new FetchMovieTask(adapter, null);
            task.execute(sortOrder, Constants.API_KEY, String.valueOf(page));
        }
        adapter.notifyDataSetChanged();

    }


    public void launchYouTube(View view) {
        if (view == null) {
            Log.d(LOG_TAG, "There are no tag the view is null. ");
        } else {
            ImageButton button = (ImageButton) view;
            String key = button.getTag().toString();
            Log.d(LOG_TAG, "The tag is: " + button.getTag());


            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key))
                        ;
                startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + key));
                startActivity(intent);
            }
        }
    }


    public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            String status = NetworkUtil.getConnectivityStatusString(context);

            Intent shareIntent = new Intent(context, MainActivity.class);
            //context.startActivity(shareIntent);
            Log.d(LOG_TAG, "Net work status is: " + status);
            if (!Constants.NET_STATUS_NOT_CONNECTED.equals(NetworkUtil.getConnectivityStatusString(context))) {
                fetchData();
//                if (rootView != null) {
//                    TextView tv = (TextView) rootView.findViewById(R.id.tv_no_data);
//                    tv.setText("");
//                }
            }
        }
    }

}
