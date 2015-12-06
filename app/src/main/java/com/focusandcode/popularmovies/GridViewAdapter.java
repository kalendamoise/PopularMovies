package com.focusandcode.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.focusandcode.popularmovies.Entities.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Moise2022 on 9/25/15.
 */
public class GridViewAdapter  extends ArrayAdapter<Movie> {
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


    static class ViewHolder {
        @Bind(R.id.title) TextView imageTitle;
        @Bind(R.id.imageView) ImageView image;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}