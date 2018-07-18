package com.seventhstar.popularmovies.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.seventhstar.popularmovies.MainActivity;
import com.seventhstar.popularmovies.model.Movie;
import com.seventhstar.popularmovies.model.Movies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressLint("StaticFieldLeak")
public class GetMoviesTask extends AsyncTask<Void, Void, List<Movie>> {
    private final static String BASE_URL = "http://api.themoviedb.org/";
    private final static String API_VERSION = "3";
    private final String mSortBy;
    private final String mApiKey;
    private final TaskCompleteNotify mCommand;

    public GetMoviesTask(String sortBy, TaskCompleteNotify command, String apiKey, MainActivity mainActivity) {
        mSortBy = sortBy;
        mApiKey = apiKey;
        mCommand = command;
    }

    public interface Listener {
        void onLoadFinished(Command command);
    }

    public static class TaskCompleteNotify implements Command {
        private final GetMoviesTask.Listener mListener;
        private List<Movie> moviesList;

        public TaskCompleteNotify(GetMoviesTask.Listener listener) {
            mListener = listener;
        }

        @Override
        public void execute() {
            mListener.onLoadFinished(this);
        }

        public List<Movie> getMovies() {
            return moviesList;
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (movies != null) {
            mCommand.moviesList = movies;
            mCommand.execute();
        } else {
            mCommand.moviesList = new ArrayList<>();
        }
    }

    @Override
    protected List<Movie> doInBackground(Void... voids) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDbApi movieApi = retrofit.create(MovieDbApi.class);
        Call<Movies> call = movieApi.discoverMovies(mSortBy, API_VERSION, mApiKey);
        try {
            Response<Movies> response = call.execute();
            Movies movies = response.body();
            return movies != null ? movies.getMovies() : null;

        } catch (IOException e) {
           // Toast.makeText(context, context.getResources().getString(R.string.api_movie_db_problem), Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
