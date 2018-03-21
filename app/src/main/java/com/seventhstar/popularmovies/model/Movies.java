package com.seventhstar.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Movies {
        @SerializedName("results")
        private final List<Movie> moviesList = new ArrayList<>();

        public List<Movie> getMovies() {
            return moviesList;
        }
}
