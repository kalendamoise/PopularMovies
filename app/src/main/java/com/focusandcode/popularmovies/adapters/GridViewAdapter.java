package com.focusandcode.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.focusandcode.popularmovies.utils.Constants;
import com.focusandcode.popularmovies.DetailActivity;
import com.focusandcode.popularmovies.DetailActivityFragment;
import com.focusandcode.popularmovies.Entities.Movie;
import com.focusandcode.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Moise2022 on 9/25/15.
 */
public class GridViewAdapter extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {
    private static final String LOG_TAG = GridViewAdapter.class.getName();
    private List<Movie> movies;
    private Context context;
    private boolean twoPane;
    private FragmentManager fragmentManager;


    public GridViewAdapter(Context context, List<Movie> movies, boolean mTwoPane, FragmentManager fragmentManager){
        this.movies = movies;
        this.context = context;
        this.twoPane = mTwoPane;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gridview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.movie = movies.get(position);


        StringBuilder builder = new StringBuilder();
        builder.append(Constants.IMAGE_BASE_URL)
                    .append(Constants.SEPARATOR)
                    .append(Constants.IMAGE_SIZE)
                    .append(Constants.SEPARATOR)
                    .append(holder.movie.getPosterPath());
        String uri = builder.toString();


        Picasso.with(context)
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.image);
        holder.imageTitle.setText(holder.movie.getTitle());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "the twoPane is " + twoPane);
                if (twoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(DetailActivityFragment.ARG_ITEM_ID, holder.movie );
                    DetailActivityFragment fragment = new DetailActivityFragment();
                    fragment.setArguments(arguments);
                    if (fragmentManager.getFragments() != null) {
                        fragmentManager.getFragments().remove(0);
                    }

                    fragmentManager.beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(DetailActivityFragment.ARG_ITEM_ID, holder.movie);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public List<Movie> getMovies() {
        return movies;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        @Bind(R.id.title) TextView imageTitle;
        @Bind(R.id.imageView) ImageView image;
        public Movie movie;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + imageTitle.getText() + "'";
        }
    }

        /*ArrayAdapter<Movie> {
    private static final String LOG_TAG = GridViewAdapter.class.getName();

    private List<Movie> movies;
    private Context context;
    private int layoutResourceId;

    public GridViewAdapter(Context context, int layoutResourceId, List<Movie> movies) {
        super(context, layoutResourceId, movies);
        this.context = context;
        this.movies = movies;
        this.layoutResourceId = layoutResourceId;
    }


    @Override
    public View getView(int position, View row, ViewGroup parent) {

        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Movie movie = getItem(position);
        if (movie != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(Constants.IMAGE_BASE_URL)
                    .append(Constants.SEPARATOR)
                    .append(Constants.IMAGE_SIZE)
                    .append(Constants.SEPARATOR)
                    .append(movie.getPosterPath());
            String uri = builder.toString();
            if (holder.image != null) {
                Picasso.with(context)
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.image);
                holder.imageTitle.setText(movie.getTitle());
            }
        }

        return row;
    }


    public List<Movie> getMovies() {
        return movies;
    }


    static class ViewHolder  {
        @Bind(R.id.title) TextView imageTitle;
        @Bind(R.id.imageView) ImageView image;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    */
}