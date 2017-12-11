package com.harrysilman.moviecollection.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.harrysilman.moviecollection.R;
import com.harrysilman.moviecollection.data.DatabaseDescription.Movie;

public class MovieCollectionContentProvider extends ContentProvider {

    //used to access the database
    private MovieCollectionDatabaseHelper dbHelper;

    //UriMatcher helps ContentProvider determine operation to perform
    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    //constants used with UriMathcer to determine operation to perform
    private static final int ONE_MOVIE = 1; //manipulate one movie
    private static final int MOVIES = 2; //manipulate movies table

    //static block to configure this ContentProvider's UriMatcher
    static {
        //Uri for Movie with the specified id (#)
        uriMatcher.addURI(DatabaseDescription.AUTHORITY,
                Movie.TABLE_NAME + "/#", ONE_MOVIE);

        //Uri for Movies tables
        uriMatcher.addURI(DatabaseDescription.AUTHORITY,
                Movie.TABLE_NAME, MOVIES);
    }

    @Override
    public boolean onCreate() {
        //create the MovieCollectionDatabaseHelper
        dbHelper = new MovieCollectionDatabaseHelper(getContext());
        return true; //return true if successful
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        //create the SQLiteQueryBuilder
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Movie.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case ONE_MOVIE: //movie with specified id will be selected
                queryBuilder.appendWhere(
                        Movie._ID + "=" + uri.getLastPathSegment());
                break;
            case MOVIES: //all movies will be selected
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_query_uri) + uri);
        }

        //execute the query
        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null,null, sortOrder);

        //configure to watch for content changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    //insert new movie into db
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri newMovieUri = null;

        switch(uriMatcher.match(uri)) {
            case MOVIES:
                //insert the new contact
                long rowID = dbHelper.getWritableDatabase().insert(
                        Movie.TABLE_NAME, null, values);

                //if the movie was inserted, create a Uri, else throw exception
                if (rowID > 0) { //SQLite row IDs start at 1
                    newMovieUri = Movie.buildMovieUri(rowID);

                    //notify listeners of database change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                else
                    throw new SQLException(
                            getContext().getString(R.string.insert_failed) + uri);
                    break;

                 default:
                     throw new UnsupportedOperationException(
                             getContext().getString(R.string.invalid_insert_uri) + uri);
        }
        return newMovieUri;
    }

    //update existing movie in db
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int numberOfRowsUpdated; //1 if update successful, 0 otherwise

        switch (uriMatcher.match(uri)) {
            case ONE_MOVIE:
                //get ID from uri
                String id = uri.getLastPathSegment();

                //update the movie
                numberOfRowsUpdated = dbHelper.getWritableDatabase().update(
                        Movie.TABLE_NAME, values, Movie._ID + "=" + id,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_update_uri) + uri);
        }

        //if changes were made, notify listeners
        if (numberOfRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numberOfRowsUpdated;
    }

    //delete an existing movie
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numberOfRowsDeleted;

        switch (uriMatcher.match(uri)) {
            case ONE_MOVIE:
                //get the id from uri
                String id = uri.getLastPathSegment();

                //delete the movie
                numberOfRowsDeleted = dbHelper.getWritableDatabase().delete(
                        Movie.TABLE_NAME, Movie._ID + "=" + id, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(
                        getContext().getString(R.string.invalid_delete_uri) + uri);
        }

        // notify listeners of db change
        if (numberOfRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfRowsDeleted;
    }













}
