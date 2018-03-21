package com.seventhstar.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.seventhstar.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rm on 20.03.2018.
 */

class MovieAdapter extends ArrayAdapter<Movie> {
    List<Movie> moviesList;

    public MovieAdapter(@NonNull Context context, List<Movie> moviesList) {
        super(context, 0, moviesList);
        this.moviesList = moviesList;
    }

    public MovieAdapter(MainActivity mainActivity) {
        super(mainActivity, 0);
        this.moviesList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return moviesList.size();
    }

    @Nullable
    @Override
    public Movie getItem(int position) {
        return moviesList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie androidFlavor = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);
        }

        Movie movie = getItem(position);

        ImageView iconView = convertView.findViewById(R.id.movie_poster);
        iconView.setImageResource(R.drawable.donut);
        //setImageResource(R.drawable.n1);

        TextView versionNameView = convertView.findViewById(R.id.flavor_text);
        versionNameView.setText(movie.getTitle());

        return convertView;
    }

    public void refresh(List<Movie> moviesList) {
        this.moviesList = moviesList;
        this.notifyDataSetChanged();
    }
}
