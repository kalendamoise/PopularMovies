package com.focusandcode.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.focusandcode.popularmovies.Entities.MovieVideo;
import com.focusandcode.popularmovies.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Moise2022 on 12/16/15.
 */
public class MovieVideoAdapter extends ArrayAdapter<MovieVideo> {
    public List<MovieVideo> getVideos() {
        return videos;
    }

    private List<MovieVideo> videos;
    private Context context;
    private int layoutResourceId;

    public MovieVideoAdapter(Context context, int layoutResourceId, List<MovieVideo> videos) {
        super(context, layoutResourceId, videos);
        this.context = context;
        this.videos = videos;
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

        MovieVideo movie = getItem(position);
        if (movie != null) {
            holder.titleTextView.setText(movie.getName());
            holder.videoKey.setText(movie.getKey());
            holder.imageButton.setTag(movie.getKey());
        }
        return row;
    }

    public static class ViewHolder {
        @Bind(R.id.list_item_video_title) public TextView titleTextView;
        @Bind(R.id.list_item_video_key) public TextView videoKey;
        @Bind(R.id.list_item_video_image) public ImageButton imageButton;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}


