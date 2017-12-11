package com.harrysilman.moviecollection.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.harrysilman.moviecollection.data.DatabaseDescription.Movie;

/**
 * Created by Silman on 12/7/2017.
 */

class MovieCollectionDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MovieCollection.db";
    private static final int DATABASE_VERSION = 1;

    //constructor
    public MovieCollectionDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //creates the movies table when the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL for creating the movies table
        final String CREATE_MOVIES_TABLE =
                "CREATE TABLE " + Movie.TABLE_NAME + "(" +
                        Movie._ID + " integer primary key, " +
                        Movie.COLUMN_TITLE + " TEXT, " +
                        Movie.COLUMN_YEAR + " TEXT, " +
                        Movie.COLUMN_DIRECTOR + " TEXT);";
        db.execSQL(CREATE_MOVIES_TABLE); //create the movies table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
