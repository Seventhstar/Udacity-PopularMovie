package com.seventhstar.popularmovies.service;

import com.seventhstar.popularmovies.model.Movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface MovieDbApi {
    @GET("{api_version}/movie/{sort_by}")
    Call<Movies> discoverMovies(@Path("sort_by") String sortBy, @Path("api_version") String apiVersion, @Query("api_key") String apiKey);
}