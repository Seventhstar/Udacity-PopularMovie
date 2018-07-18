package com.seventhstar.popularmovies.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Arrays;

public final class MovieEntry implements BaseColumns {
    public static final Uri CONTENT_URI = DBContract.BASE_CONTENT_URI.buildUpon().appendPath(DBContract.PATH).build();
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + DBContract.AUTHORITY
            + "/" + DBContract.PATH;

    public static final String TABLE_NAME = "movies";

    public static final String COLUMN_MOVIE_API_ID = "API_ID";
//    public static final String COLUMN_MOVIE_NAME = "movieName";
//    public static final String COLUMN_MOVIE_OVERVIEW = "movieOverview";
//    public static final String COLUMN_MOVIE_RELEASE_DATE = "movieReleaseDate";
//    public static final String COLUMN_MOVIE_RATING = "movieRating";
//    public static final String COLUMN_MOVIE_POPULARITY = "moviePopularity";
//    public static final String COLUMN_MOVIE_SMALL_POSTER = "movieSmallPoster";
//    public static final String COLUMN_MOVIE_POSTER = "moviePoster";
//    public static final String COLUMN_MOVIE_GENRES = "genres";


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
