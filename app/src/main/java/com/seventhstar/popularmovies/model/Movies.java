package com.seventhstar.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rm on 20.03.2018.
 */

public class Movies {

        @SerializedName("results")
        private List<Movie> moviesList = new ArrayList<>();

        public List<Movie> getMovies() {
            return moviesList;
        }



}
