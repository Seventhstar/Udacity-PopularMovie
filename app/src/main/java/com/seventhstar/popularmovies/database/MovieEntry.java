package com.seventhstar.popularmovies.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class MovieEntry implements BaseColumns {
    public static final Uri CONTENT_URI = DBContract.BASE_CONTENT_URI.buildUpon().appendPath(DBContract.PATH).build();
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + DBContract.AUTHORITY
            + "/" + DBContract.PATH;

    public static final String TABLE_NAME = "movies";
    public static final String COLUMN_MOVIE_API_ID = "movieApiId";
    public static final String COLUMN_MOVIE_NAME = "movieName";
    public static final String COLUMN_MOVIE_OVERVIEW = "movieOverview";
    public static final String COLUMN_MOVIE_RELEASE_DATE = "movieReleaseDate";
    public static final String COLUMN_MOVIE_RATING = "movieRating";
    public static final String COLUMN_MOVIE_SMALL_POSTER = "movieSmallPoster";
    public static final String COLUMN_MOVIE_POSTER = "moviePoster";
    public static final String COLUMN_MOVIE_GENRES = "genres";
}
