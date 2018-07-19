package com.seventhstar.popularmovies.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import android.content.ContentUris;
import java.util.Arrays;

public final class MovieEntry implements BaseColumns {
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + DBContract.AUTHORITY
            + "/" + DBContract.PATH_MOVIES;

    public static final String CONTENT_ITEM_TYPE = ContentResolver.ANY_CURSOR_ITEM_TYPE
            + "/" + DBContract.AUTHORITY
            + "/" + DBContract.PATH_MOVIES;

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + DBContract.AUTHORITY);

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(DBContract.PATH_MOVIES).build();

    public static Uri buildMovieUri(long id){
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }


    public static final String TABLE_NAME = "movies";

    public static final String COLUMN_MOVIE_API_ID = "API_ID";

    public enum MovieColumns {
        API_ID(0),
        TITLE(1),
        OVERVIEW(2),
        RELEASE_DATE(3),
        RATING(4),
        POPULARITY(5),
        POSTER_PATH(6),
        BACKDROP_PATH(7),
        GENRES(8);

        private int value;

        MovieColumns(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static String[] columnNames() {
            return Arrays.toString(MovieColumns.values()).replaceAll("^.|.$", "").split(", ");
        }
    }
}
