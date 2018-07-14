package com.seventhstar.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Reviews {
    @SerializedName("results")
    @Expose
    private final List<Review> reviews = new ArrayList<>();

    public List<Review> getReviewsList() {
        return reviews;
    }
}
