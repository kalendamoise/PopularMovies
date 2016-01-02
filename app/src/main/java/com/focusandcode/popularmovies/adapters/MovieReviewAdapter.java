package com.focusandcode.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.focusandcode.popularmovies.Entities.MovieReview;
import com.focusandcode.popularmovies.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Moise2022 on 12/16/15.
 */
public class MovieReviewAdapter extends ArrayAdapter<MovieReview> {
    public List<MovieReview> getReviews() {
        return reviews;
    }

    private List<MovieReview> reviews;
    private Context context;
    private int layoutResourceId;

    public MovieReviewAdapter(Context context, int layoutResourceId, List<MovieReview> reviews) {
        super(context, layoutResourceId, reviews);
        this.context = context;
        this.reviews = reviews;
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

        MovieReview review = getItem(position);
        if (review != null) {
            holder.authorTextView.setText(review.getAuthor());
            holder.contentTextView.setText(review.getContent());
        }

        return row;
    }


    public static class ViewHolder {
        @Bind(R.id.list_item_review_content)  TextView contentTextView;
        @Bind(R.id.list_item_review_author)  TextView authorTextView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
