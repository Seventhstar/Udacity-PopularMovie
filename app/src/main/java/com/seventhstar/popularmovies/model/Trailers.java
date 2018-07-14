package com.seventhstar.popularmovies.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Trailers {
    @SerializedName("results")
    private final List<Trailer> trailers = new ArrayList<>();

    public List<Trailer> getTrailersList() {
        return trailers;
    }
}
