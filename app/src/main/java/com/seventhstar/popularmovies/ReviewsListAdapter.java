package com.seventhstar.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seventhstar.popularmovies.model.Review;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsListAdapter extends RecyclerView.Adapter<ReviewsListAdapter.ViewHolder> {

    private final ArrayList<Review> mReviews;

    private final Callbacks mCallbacks;

    public ReviewsListAdapter(Callbacks callbacks) {
        mReviews = new ArrayList<>();
        mCallbacks = callbacks;
    }

    public void addReviews(List<Review> reviewsList) {
        mReviews.clear();
        mReviews.addAll(reviewsList);
        notifyDataSetChanged();
    }

    public interface Callbacks {
        void read(Review review);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Review review = mReviews.get(position);

        holder.mContentView.setText(review.getContent());
        holder.mAuthorView.setText(review.getAuthor());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.read(review);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public ArrayList<Review> getReviews() {
        return mReviews;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final View mView;
        @BindView(R.id.review_content)
        TextView mContentView;
        @BindView(R.id.review_author)
        TextView mAuthorView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mView = itemView;
        }
    }
}
