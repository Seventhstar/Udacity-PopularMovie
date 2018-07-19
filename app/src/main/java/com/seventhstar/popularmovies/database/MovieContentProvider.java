package com.seventhstar.popularmovies.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieContentProvider extends ContentProvider {
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_MOVIES, DBContract.URI_MOVIES);
        uriMatcher.addURI(DBContract.AUTHORITY, DBContract.PATH_MOVIES + "/#", DBContract.URI_MOVIES_ID);
    }

    private static final int MOVIES = 122;
    private DBHelper dbHelper;


    @Override
    public boolean onCreate() {
        Context context = getContext();
        assert context != null;
        dbHelper = new DBHelper(context.getApplicationContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        int match = uriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case DBContract.URI_MOVIES:
                cursor = db.query(MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case DBContract.URI_MOVIES_ID:
                long _id = ContentUris.parseId(uri);
                cursor = db.query(MovieEntry.TABLE_NAME,
                        projection,
                        MovieEntry.COLUMN_MOVIE_API_ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        Context context = getContext();
        if (context != null) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }
        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case DBContract.URI_MOVIES:
                return MovieEntry.CONTENT_TYPE;
            case DBContract.URI_MOVIES_ID:
                return MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);

        Uri returnUri;
        switch (match) {
            case DBContract.URI_MOVIES: {
                long id = database.insert(MovieEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(DBContract.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Error insert movie.db" + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }

        Context context = getContext();
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int moviesDeleted;

        switch (match) {
            case DBContract.URI_MOVIES_ID: {
                moviesDeleted = db.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        Context context = getContext();
        if (moviesDeleted != 0 && context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIES:
                rowsUpdated = db.update(MovieEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0 && getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
