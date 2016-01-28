package com.focusandcode.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.focusandcode.popularmovies.Entities.Movie;
import com.focusandcode.popularmovies.Services.MovieService;
import com.focusandcode.popularmovies.adapters.RestClient;
import com.focusandcode.popularmovies.utils.Constants;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetailActivity.class.getName();
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.movie_backdr)
    ImageView movieBackdr;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null) {
            Movie movie = intent.getExtras().getParcelable("movie");
            Log.d(LOG_TAG, "Movie ID: " + movie.getId());

            MovieService movieService = RestClient.getMovieService();

            collapsingToolbar.setTitle(movie.getTitle());


            StringBuilder builder = new StringBuilder();
            builder.append(Constants.IMAGE_BASE_URL)
                    .append(Constants.SEPARATOR)
                    .append(Constants.IMAGE_SIZE)
                    .append(Constants.SEPARATOR)
                    .append(movie.getBackdropPath());
            String uri = builder.toString();

            Log.d(LOG_TAG, "Backdrop URL: " + uri);

            Picasso.with(getApplicationContext())
                    .load(uri)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(movieBackdr);



            if (savedInstanceState == null) {
                // Create the detail fragment and add it to the activity
                // using a fragment transaction.
                Bundle arguments = new Bundle();
                arguments.putParcelable(DetailActivityFragment.ARG_ITEM_ID, movie );
                DetailActivityFragment fragment = new DetailActivityFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.item_detail_container, fragment)
                        .commit();
            }

        }

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
}
