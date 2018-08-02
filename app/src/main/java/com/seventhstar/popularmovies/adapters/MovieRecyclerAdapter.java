package com.seventhstar.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.seventhstar.popularmovies.MainActivity;
import com.seventhstar.popularmovies.R;
import com.seventhstar.popularmovies.model.Movie;
import com.seventhstar.popularmovies.service.DBTools;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.seventhstar.popularmovies.database.MovieEntry.*;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieHolder> {
    private ArrayList<Movie> moviesList;
    private final Context context;
    private final Callbacks callbacks;

    public ArrayList<Movie> getMovies() {
        return moviesList;
    }

    private final RecycleViewOnItemClickListener recyclerViewOnClickListener;

    public interface RecycleViewOnItemClickListener {
        void onItemResultRecyclerClick(Movie movie);

        void onFavoriteClick(Movie movie);
    }


    public void fromCursor(Cursor cursor) {
        moviesList.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(
                        cursor.getString(MovieColumns.API_ID.getValue()),
                        cursor.getString(MovieColumns.TITLE.getValue()),
                        cursor.getString(MovieColumns.OVERVIEW.getValue()),
                        cursor.getString(MovieColumns.RELEASE_DATE.getValue()),
                        cursor.getString(MovieColumns.RATING.getValue()),
                        cursor.getString(MovieColumns.POPULARITY.getValue()),
                        cursor.getString(MovieColumns.POSTER_PATH.getValue()),
                        cursor.getString(MovieColumns.BACKDROP_PATH.getValue()),
                        cursor.getString(MovieColumns.GENRES.getValue()),
                        true);


                moviesList.add(movie);
            } while (cursor.moveToNext());
        }
        this.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MovieHolder(itemView, context);
    }


    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        Movie movie = moviesList.get(position);
        if (holder instanceof MovieHolder) {
            MovieHolder movieHolder = holder;
            movieHolder.bindAll(movie);
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public interface Callbacks {

        void openMovie(Movie movie);
    }

    public MovieRecyclerAdapter(MainActivity mainActivity, RecycleViewOnItemClickListener recyclerViewOnClickListener) {
        this.callbacks = mainActivity;
        this.moviesList = new ArrayList<>();
        this.context = mainActivity;
        this.recyclerViewOnClickListener = recyclerViewOnClickListener;
    }


    public void refresh(ArrayList<Movie> moviesList) {
        this.moviesList = moviesList;
        this.notifyDataSetChanged();
    }

    class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.movie_poster)
        ImageView moviePoster;

        @BindView(R.id.flavor_text)
        TextView movieTitle;

        @BindView(R.id.iv_favorite)
        ImageView ivIsFavorite;

        Context context;
        View itemView;


        MovieHolder(View itemView, Context context) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            ivIsFavorite.setOnClickListener(this);
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            if (v.getId() == R.id.iv_favorite) {
                Movie m = moviesList.get(adapterPosition);
                recyclerViewOnClickListener.onFavoriteClick(m);
                setFavoriteIcon(m.isFavorite());
            } else {
                recyclerViewOnClickListener.onItemResultRecyclerClick(
                        moviesList.get(adapterPosition));
            }
        }


        public void bindAll(Movie movie) {
            ImageView imageView = itemView.findViewById(R.id.movie_poster);
            TextView versionNameView = itemView.findViewById(R.id.flavor_text);
            TextView rating = itemView.findViewById(R.id.tv_movie_rating);

            setFavoriteIcon(DBTools.isFavorite(context, movie.getId()));

            versionNameView.setText(movie != null ? movie.getTitle() : "");
            rating.setText(movie.getRating());

            String url = movie != null ? movie.getPreviewURL() : null;
            if (url != null) {
                Picasso.with(context)
                        .load(url)
                        .into(imageView);
            }
        }

        void setFavoriteIcon(boolean isFavorite) {
            if (isFavorite) {
                ivIsFavorite.setImageResource(R.drawable.ic_favorites);
            } else {
                ivIsFavorite.setImageResource(R.drawable.ic_not_favorites);
            }
        }

    }

}
