package com.seventhstar.popularmovies.service;

import com.seventhstar.popularmovies.model.Movie;
import com.seventhstar.popularmovies.model.Movies;
import com.seventhstar.popularmovies.model.Reviews;
import com.seventhstar.popularmovies.model.Trailers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface MovieDbApi {
    @GET("{api_version}/movie/{sort_by}")
    Call<Movies> discoverMovies(@Path("sort_by") String sortBy,
                                @Path("api_version") String apiVersion,
                                @Query("api_key") String apiKey);

    @GET("{api_version}/movie/{movie_id}/videos")
    Call<Trailers> getTrailers(@Path("movie_id") long movieId,
                               @Path("api_version") String apiVersion,
                               @Query("api_key") String apiKey);

    @GET("{api_version}/movie/{movie_id}/reviews")
    Call<Reviews> getReviews(@Path("movie_id") long movieId,
                             @Path("api_version") String apiVersion,
                             @Query("api_key") String apiKey);

    @GET("{api_version}/movie/{movie_id}")
    Call<Movie> getMovieDetail(@Path("movie_id") long movieId,
                               @Path("api_version") String apiVersion,
                               @Query("api_key") String apiKey);
}