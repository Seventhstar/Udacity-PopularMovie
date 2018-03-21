package com.seventhstar.popularmovies.service;

import com.seventhstar.popularmovies.model.Movie;
import com.seventhstar.popularmovies.model.Movies;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by rm on 20.03.2018.
 */

public interface MovieDbApi {
    @GET("{api_version}/movie/{sort_by}")
    Call<Movies> discoverMovies(@Path("sort_by") String sortBy, @Path("api_version") String apiVersion, @Query("api_key") String apiKey);
}
