package com.focusandcode.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private GridViewAdapter adapter;
    private List<Movie> movies = new ArrayList<Movie>();
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new GridViewAdapter(this,R.layout.gridview_layout, movies);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);


        fetchdata();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                fetchdata();
            }
        });


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Movie item = (Movie) parent.getItemAtPosition(position);
                Log.d(LOG_TAG, item.toString());
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("movie", item);
                startActivity(intent);

            }
        });

        preferences =  PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void fetchdata() {

        String sortOrder = getString(R.string.pref_sort_order_default);
        if (preferences != null) {
            sortOrder = preferences.getString(
                    getString(R.string.pref_title_sort_order),
                    getString(R.string.pref_sort_order_default));
        }


        FetchMovieTask task = new FetchMovieTask(adapter);
        task.execute(sortOrder, Constants.API_KEY);
        adapter.notifyDataSetChanged();

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
}
