package com.harrysilman.moviecollection.data;

/**
 * Created by Silman on 12/7/2017.
 */

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseDescription {

    //ContentProvider's name
    public static final String AUTHORITY =
            "com.harrysilman.moviecollection.data";

    //base URI used to interact with ContentProvider
    private static final Uri BASE_CONTENT_URI =
            Uri.parse("content://" + AUTHORITY);

    //nested class to define the contents of the movies table
    public static final class Movie implements BaseColumns {
        public static final String TABLE_NAME = "movies"; // name for the table

        //Uri for the movies table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        //column names for the movies table's columns
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_DIRECTOR = "director";

        //creates a Uri for a specific movie
        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
