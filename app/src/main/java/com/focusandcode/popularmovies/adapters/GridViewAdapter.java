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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.focusandcode.popularmovies.DetailActivity;
import com.focusandcode.popularmovies.DetailActivityFragment;
import com.focusandcode.popularmovies.Entities.Movie;
import com.focusandcode.popularmovies.R;
import com.focusandcode.popularmovies.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Moise2022 on 9/25/15.
 */
public class GridViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String LOG_TAG = GridViewAdapter.class.getName();
    private final int VIEW_ITEM = 0;
    private final int VIEW_PROG = 1;
    private List<Movie> movies = new ArrayList<Movie>();
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.gridview_layout, parent, false);
//        return new ViewHolder(view);

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.gridview_layout, parent, false);

            vh = new ViewHolder(v);

        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        Log.d(LOG_TAG, "View Type: " + viewType);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof GridViewAdapter.ViewHolder) {
            final GridViewAdapter.ViewHolder viewHolder =(GridViewAdapter.ViewHolder) holder;
            viewHolder.movie = movies.get(position);


            StringBuilder builder = new StringBuilder();
            builder.append(Constants.IMAGE_BASE_URL)
                    .append(Constants.SEPARATOR)
                    .append(Constants.IMAGE_SIZE)
                    .append(Constants.SEPARATOR)
                    .append(viewHolder.movie.getPosterPath());
            String uri = builder.toString();


            Picasso.with(context)
                    .load(uri)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(viewHolder.image);
            viewHolder.imageTitle.setText(viewHolder.movie.getTitle());

            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "the twoPane is " + twoPane);
                    if (twoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelable(DetailActivityFragment.ARG_ITEM_ID, viewHolder.movie);
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
                        intent.putExtra(DetailActivityFragment.ARG_ITEM_ID, viewHolder.movie);
                        context.startActivity(intent);
                    }
                }
            });
        }else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public List<Movie> getMovies() {
        if (movies == null) {
            movies = new ArrayList<Movie>();
        }
        return movies;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
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

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}