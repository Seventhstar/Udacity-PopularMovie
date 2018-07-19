package com.seventhstar.popularmovies.database;

import android.content.ContentUris;
import android.net.Uri;

public class DBContract {
    @SuppressWarnings("SpellCheckingInspection")
    public static final String AUTHORITY = "com.seventhstar.udacity.popular_movies";
    public static final String PATH_MOVIES = "movies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

    public static Uri buildMovieUri(long id){
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    public static final int URI_MOVIES = 100;
    public static final int URI_MOVIES_ID = 101;


}
