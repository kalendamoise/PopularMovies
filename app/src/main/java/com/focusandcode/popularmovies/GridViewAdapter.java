package com.focusandcode.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

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
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.title);
            holder.image = (ImageView) row.findViewById(R.id.imageView);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Movie movie = (Movie) getItem(position);
        if (movie != null) {

            String uri = Constants.IMAGE_BASE_URL + "/" + Constants.IMAGE_SIZE + "/" + movie.getPosterPath();
            if (holder.image != null) {
                Picasso.with(context).load(uri).into(holder.image);
                holder.imageTitle.setText(movie.getTitle());
            }
        }

        return row;
    }


    public List<Movie> getMovies() {
        return movies;
    }


    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}