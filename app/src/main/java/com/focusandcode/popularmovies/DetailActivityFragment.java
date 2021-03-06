package com.focusandcode.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.focusandcode.popularmovies.Data.MoviesContract;
import com.focusandcode.popularmovies.Entities.ListMovieReviews;
import com.focusandcode.popularmovies.Entities.ListMovieVideos;
import com.focusandcode.popularmovies.Entities.Movie;
import com.focusandcode.popularmovies.Entities.MovieReview;
import com.focusandcode.popularmovies.Entities.MovieVideo;
import com.focusandcode.popularmovies.Services.MovieService;
import com.focusandcode.popularmovies.adapters.MovieReviewAdapter;
import com.focusandcode.popularmovies.adapters.MovieVideoAdapter;
import com.focusandcode.popularmovies.adapters.RestClient;
import com.focusandcode.popularmovies.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String ARG_ITEM_ID = "movie";
    private static final String LOG_TAG = DetailActivityFragment.class.getName();
    private static final int CURSOR_LOADER_ID = 0;
    private Movie movie;
    private int mPosition;
    private Uri mUri = MoviesContract.MovieEntry.CONTENT_URI;
    private List<MovieReview> movieReviews = new ArrayList<MovieReview>();
    private List<MovieVideo> movieVideos = new ArrayList<MovieVideo>();
    private ShareActionProvider mShareActionProvider;


    @Bind(R.id.reviews_container)
    LinearLayout reviewContainer;
    @Bind(R.id.videos_container)
    LinearLayout videoContainer;
    @Bind(R.id.movie_poster)
    ImageView imageView;
    @Bind(R.id.original_title)
    TextView originalTitle;
    @Bind(R.id.release_date)
    TextView releaseDate;
    @Bind(R.id.plot_synopsis)
    TextView plotSynopsis;
    @Bind(R.id.add_to_favorite)
    ImageButton addToFavorite;


    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }


    public static DetailActivityFragment newInstance(int position, Uri uri) {
        DetailActivityFragment fragment = new DetailActivityFragment();
        Bundle args = new Bundle();
        fragment.mPosition = position;
        Log.d(LOG_TAG, "Invoke newInstance ...");
        fragment.mUri = uri;
        args.putInt("id", position);
        fragment.setArguments(args);
        return fragment;
    }

    public void launchYouTube(View view) {

        Log.d(LOG_TAG, "The tag is: " + view.getTag().toString());

//        MovieVideo movieVideo = (MovieVideo) adapterView.getItemAtPosition(position);
//        try {
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + movieVideo.getKey()));
//            startActivity(intent);
//        } catch (ActivityNotFoundException ex) {
//            Intent intent = new Intent(Intent.ACTION_VIEW,
//                    Uri.parse("http://www.youtube.com/watch?v=" + movieVideo.getKey()));
//            startActivity(intent);
//        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);


        if (getArguments().containsKey(ARG_ITEM_ID)) {
            movie = getArguments().getParcelable(ARG_ITEM_ID);
            Log.d(LOG_TAG, "Movie ID: " + movie.getId());

            MovieService movieService = RestClient.getMovieService();
            // Fetch reviews and videos
            movieService.getMovieVideos((long) movie.getId(), Constants.API_KEY, movieVideosCallback);
            movieService.getMovieReviews((long) movie.getId(), Constants.API_KEY, movieReviewsCallback);

            final Context context = getActivity().getApplicationContext();

//            Activity activity = this.getActivity();
//            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
//            if (appBarLayout != null) {
//                appBarLayout.setTitle(movie.getTitle());
//            }
            //collapsingToolbar.setTitle(movie.getTitle());

//            ImageView movieBackdr = (ImageView) activity.findViewById(R.id.movie_backdr);
//
//
//
//            StringBuilder builder = new StringBuilder();
//            builder.append(Constants.IMAGE_BASE_URL)
//                    .append(Constants.SEPARATOR)
//                    .append(Constants.IMAGE_SIZE)
//                    .append(Constants.SEPARATOR)
//                    .append(movie.getBackdropPath());
//            String uri = builder.toString();
//
//            Log.d(LOG_TAG, "Backdrop URL: " + uri);
//
//            final Context context = getActivity().getApplicationContext();
//
//            Picasso.with(context)
//                    .load(uri)
//                    .placeholder(R.mipmap.ic_launcher)
//                    .error(R.mipmap.ic_launcher)
//                    .into(movieBackdr);

            StringBuilder builder = new StringBuilder();
            builder.append(Constants.IMAGE_BASE_URL)
                    .append(Constants.SEPARATOR)
                    .append(Constants.IMAGE_SIZE)
                    .append(Constants.SEPARATOR)
                    .append(movie.getPosterPath());
            String uri = builder.toString();

            Log.d(LOG_TAG, "Poster URL: " + uri);
            Log.d(LOG_TAG, "Movie ID: " + movie.getId());


            Picasso.with(context)
                    .load(uri)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);


            originalTitle.setText(movie.getOriginalTitle());

            releaseDate.setText(movie.getReleaseDate().trim());
            plotSynopsis.setText(movie.getOverview());


            TextView rating = (TextView) rootView.findViewById(R.id.rating);
            rating.setText(String.valueOf(movie.getVoteAverage()));

        }

        Log.d(LOG_TAG, movie.toString());

        // check to see if the movie is present in the database and toggle the favorite image button accordingly.
        String selection = MoviesContract.MovieEntry._ID;
        String[] selectionArgs = new String[]{String.valueOf(movie.getId())};
        Cursor cursor = getActivity().getContentResolver().query(MoviesContract.MovieEntry.buildFlavorsUri(movie.getId()), null, selection, selectionArgs, null);

        if (cursor.getCount() == 0) {
            addToFavorite.setImageResource(android.R.drawable.btn_star_big_off);
        } else {
            addToFavorite.setImageResource(android.R.drawable.btn_star_big_on);
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detail_activity, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mShareActionProvider != null) {
            //mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        //shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "Title: " + movie.getTitle() + " -- Synopsis: " + movie.getOverview()
                        + " -- trailer: http://www.youtube.com/watch?v=" + movieVideos.get(0).getKey());
        return shareIntent;
    }

    @OnClick(R.id.add_to_favorite)
    public void addToFavorite(View view) {


        String selection = MoviesContract.MovieEntry._ID;
        String[] selectionArgs = new String[]{String.valueOf(movie.getId())};


        Cursor cursor = getActivity().getContentResolver().query(MoviesContract.MovieEntry.buildFlavorsUri(movie.getId()), null, selection, selectionArgs, null);

        if (cursor.getCount() == 0) {

            // change the button to indicate the image was added to the favorite
            addToFavorite.setImageResource(android.R.drawable.btn_star_big_on);
            Log.d(LOG_TAG, "Add to favorite called");

            ContentValues contentValue = new ContentValues();
            contentValue.put(MoviesContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
            contentValue.put(MoviesContract.MovieEntry._ID, movie.getId());
            contentValue.put(MoviesContract.MovieEntry.COLUMN_ADULT, movie.isAdult());
            contentValue.put(MoviesContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
            contentValue.put(MoviesContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
            contentValue.put(MoviesContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
            contentValue.put(MoviesContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
            contentValue.put(MoviesContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
            contentValue.put(MoviesContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
            contentValue.put(MoviesContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
            contentValue.put(MoviesContract.MovieEntry.COLUMN_VIDEO, movie.isVideo());
            contentValue.put(MoviesContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
            contentValue.put(MoviesContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());

            getActivity().getContentResolver().insert(MoviesContract.MovieEntry.CONTENT_URI,
                    contentValue);
        } else {
            Log.d(LOG_TAG, "This movie is already in the database. The count was: " + cursor.getCount());
            addToFavorite.setImageResource(android.R.drawable.btn_star_big_off);
            int rowDeleted = getActivity().getContentResolver().delete(MoviesContract.MovieEntry.CONTENT_URI, MoviesContract.MovieEntry._ID + " = " + movie.getId(), null);
            if (rowDeleted > 0) {
                Log.d(LOG_TAG, "The movie " + movie.getTitle() + " was successfully remove from favorite.");
            }
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = null;
        String[] selectionArgs = null;
        if (args != null) {
            selection = MoviesContract.MovieEntry._ID;
            selectionArgs = new String[]{String.valueOf(movie.getId())};
        }
        return new CursorLoader(getActivity(),
                mUri,
                null,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Cursor c =
                getActivity().getContentResolver().query(MoviesContract.MovieEntry.CONTENT_URI,
                        new String[]{MoviesContract.MovieEntry._ID},
                        null,
                        null,
                        null);
        if (c.getCount() == 0) {
            //insertData();
        }
        // initialize loader
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(LOG_TAG, "The cursor data is: " + data.toString());
        Log.d(LOG_TAG, "The count was: " + data.getCount());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(LOG_TAG, "Cursor data has been reset");
    }

    @Override
    public void onResume() {
        super.onResume();
        //new RestClient().runRetrofitTestSync(Long.getLong(movie.getId() + "", 1L));
    }


    private Callback<ListMovieReviews> movieReviewsCallback = new Callback<ListMovieReviews>() {
        @Override
        public void success(ListMovieReviews data, Response response) {
            Log.d(LOG_TAG, "Reviews: " + data.toString());
            movieReviews = data.getResults();
            for (int i = 0; i < movieReviews.size(); i++) {
                MovieReview review = movieReviews.get(i);
                if (reviewContainer != null)
                    reviewContainer.addView(buildReview(review));
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d(LOG_TAG, "failure: " + error);
        }
    };

    private Callback<ListMovieVideos> movieVideosCallback = new Callback<ListMovieVideos>() {
        @Override
        public void success(ListMovieVideos data, Response response) {
            Log.d(LOG_TAG, "Videos: " + data.toString());
            movieVideos = data.getResults();
            for (int i = 0; i < movieVideos.size(); i++) {
                MovieVideo video = movieVideos.get(i);
                if (videoContainer != null) {
                    videoContainer.addView(buildVideo(video));
                    if (i == 0) {
                        if (mShareActionProvider != null) {
                            mShareActionProvider.setShareIntent(createShareForecastIntent());
                        }
                    }
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d(LOG_TAG, "failure: " + error);
        }
    };


    public View buildReview (MovieReview review) {
        View reviewView = getActivity().getLayoutInflater()
                .inflate(R.layout.movie_reviews, null);
        MovieReviewAdapter.ViewHolder viewHolder = new MovieReviewAdapter.ViewHolder(reviewView);
        viewHolder.authorTextView.setText(review.getAuthor());
        viewHolder.contentTextView.setText(review.getContent());
        return reviewView;
    }

    public View buildVideo (MovieVideo video) {

        View videoView = getActivity().getLayoutInflater()
                .inflate(R.layout.movie_videos, null);

        MovieVideoAdapter.ViewHolder viewHolder = new MovieVideoAdapter.ViewHolder(videoView);
        viewHolder.titleTextView.setText(video.getName());
        viewHolder.imageButton.setTag(video.getKey());

        return videoView;
    }
}
