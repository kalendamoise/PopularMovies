package com.focusandcode.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Moise2022 on 9/25/15.
 */
public class GridViewAdapter  extends BaseAdapter {
    private static final String LOG_TAG = GridViewAdapter.class.getName();

    private List<Movie> movies;
    private Context context;

    public GridViewAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }


    @Override
    public int getCount() {
        return movies != null ? movies.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return movies != null && movies.size() > position ? movies.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(525, 800));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }


        if (getItem(position) != null) {

            if (imageView != null) {
                String uri = Constants.IMAGE_BASE_URL + "/" + Constants.IMAGE_SIZE + "/" + ((Movie)getItem(position)).getPosterPath();

                Picasso.with(context).load(uri)
                        .into(imageView);
                Log.d(LOG_TAG, "done updating the image view for: " + uri);
            }
        }

        return imageView;
    }


    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        Log.d( LOG_TAG, "Init movies");
    }
}