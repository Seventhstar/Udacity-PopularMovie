package com.seventhstar.popularmovies.service;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.seventhstar.popularmovies.database.DBContract;
import com.seventhstar.popularmovies.database.MovieEntry;
import com.seventhstar.popularmovies.model.Movie;

public class DBTools {


    @SuppressLint("StaticFieldLeak")
    public static void makeFavorite(final Context context, final Movie movie) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (!isFavorite(context, movie.getId())) {
                    ContentValues contentValues = new ContentValues();
                    String[] columnNames = MovieEntry.MovieColumns.columnNames();
                    for (int i = 0; i < 8; i++) {
                        contentValues.put(columnNames[i], movie.fieldByColumnId(i));
                    }
                    context.getContentResolver().insert(DBContract.CONTENT_URI, contentValues);
                }
                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
               // movie.setFavorite(true);
                //updateFavoriteButton();

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @SuppressLint("StaticFieldLeak")
    public static void removeFromFavorite(final Context context, final Movie movie) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                context.getContentResolver().delete(
                        DBContract.buildMovieUri(movie.getId()),
                        MovieEntry.COLUMN_MOVIE_API_ID + " = " + movie.getId(),
                        null);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
               // isFavorite = false;
//               // updateFavoriteButton();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static boolean isFavorite(final Context context, long movieID) {
        Cursor movieCursor = context.getContentResolver().query(
                DBContract.buildMovieUri(movieID),
                new String[]{MovieEntry.COLUMN_MOVIE_API_ID},
                null,
                null,
                null);

        if (movieCursor != null && movieCursor.moveToFirst()) {
            movieCursor.close();
            return true;
        } else {
            return false;
        }
    }

}
