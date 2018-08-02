package com.seventhstar.popularmovies.database;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.seventhstar.popularmovies.model.Movie;

class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "movies.db";
//    ContentResolver resolver = context.getContentResolver();

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String[] columnNames = MovieEntry.MovieColumns.columnNames();
        final String SQL_CREATE_FAVORITE_MOVIES_TABLE = "CREATE TABLE " +
                MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                columnNames[0] + " INTEGER NOT NULL UNIQUE, " +
                columnNames[1] + " TEXT NOT NULL, " +
                columnNames[2] + " TEXT, " +
                columnNames[3] + " DOUBLE, " +
                columnNames[4] + " DOUBLE, " +
                columnNames[5] + " TEXT, " +
                columnNames[6] + " TEXT NOT NULL, " +
                columnNames[7] + " TEXT NOT NULL, " +
                columnNames[8] + " TEXT, " +
                " UNIQUE (" +
                columnNames[0] +
                ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }


}
