package com.seventhstar.popularmovies.database;

import android.net.Uri;

class DBContract {
    @SuppressWarnings("SpellCheckingInspection")
    public static final String AUTHORITY = "com.seventhstar.udacity.popular_movies";
    public static final String PATH = "movies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
}
