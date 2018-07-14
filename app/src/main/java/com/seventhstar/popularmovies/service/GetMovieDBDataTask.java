package com.seventhstar.popularmovies.service;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.seventhstar.popularmovies.R;
import com.seventhstar.popularmovies.model.Movie;
import com.seventhstar.popularmovies.model.Reviews;
import com.seventhstar.popularmovies.model.Trailers;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetMovieDBDataTask<V> extends AsyncTask<Void, Void, V> {
    private final static String BASE_URL = "http://api.themoviedb.org/";
    private final static String API_VERSION = "3";
    private final TaskCompleteNotify mCommand;
    private TaskNames taskName;
    private final long paramId;
    private final Class<V> typeParameterClass;
    private final Context context;

    public enum TaskNames {MOVIE, TRAILERS, REVIEWS}

    public GetMovieDBDataTask(long paramId, TaskCompleteNotify command, Context context, Class<V> typeParameterClass) {
        this.paramId = paramId;
        this.mCommand = command;
        this.context = context;
        this.typeParameterClass = typeParameterClass;
    }

    public interface Listener {
        void onLoadFinished(Command command);
    }

    public static class TaskCompleteNotify<V> implements Command {
        private final Listener mListener;
        private V list;
        public TaskNames taskName;

        public TaskCompleteNotify(Listener listener) {
            mListener = listener;
        }

        @Override
        public void execute() {
            mListener.onLoadFinished(this);
        }

        public V getDataList() {
            return list;
        }
    }

    @Override
    protected void onPostExecute(V items) {
        if (items != null) {
            mCommand.list = items;
            mCommand.taskName = taskName;
            mCommand.execute();
        } else {
            mCommand.list = null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected V doInBackground(Void... voids) {
        String apiKey = context.getString(R.string.api_key);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDbApi movieApi = retrofit.create(MovieDbApi.class);
        Call<V> call = null;

        if (typeParameterClass.equals(Trailers.class)) {
            call = (Call<V>) movieApi.getTrailers(paramId, API_VERSION, apiKey);
            taskName = TaskNames.TRAILERS;
        } else if (typeParameterClass.equals(Reviews.class)) {
            call = (Call<V>) movieApi.getReviews(paramId, API_VERSION, apiKey);
            taskName = TaskNames.REVIEWS;
        } else if (typeParameterClass.equals(Movie.class)) {
            call = (Call<V>) movieApi.getMovieDetail(paramId, API_VERSION, apiKey);
            taskName = TaskNames.MOVIE;
        }

        try {
            if (call != null) {
                Response<V> response = call.execute();
                return response.body();
            } else {
                return null;
            }

        } catch (IOException e) {
            Toast.makeText(context, context.getResources().getString(R.string.api_movie_db_problem), Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
