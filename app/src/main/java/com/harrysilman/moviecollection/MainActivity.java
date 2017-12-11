package com.harrysilman.moviecollection;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity
    implements MoviesFragment.MoviesFragmentListener,
    DetailFragment.DetailFragmentListener,
    AddEditFragment.AddEditFragmentListener {

    //key for storing the movie's Uri in a Bundle passed to a fragment
    public static final String MOVIE_URI = "movie_uri";

    private MoviesFragment moviesFragment; //displays movie list

    // display MoviesFragment when MainActivity first loads
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

            //create MoviesFragment
            moviesFragment = new MoviesFragment();

            //add the fragment to the FrameLayout
            FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentContainer, moviesFragment);
            transaction.commit();
    }

    //display DetailFragment for selected movie
    @Override
    public void onMovieSelected(Uri movieUri) {
        if (findViewById(R.id.fragmentContainer) != null)
            displayMovie(movieUri, R.id.fragmentContainer);
    }

    //displayAddEditFragment to add a new movie
    @Override
    public void onAddMovie() {
        if (findViewById(R.id.fragmentContainer) != null)
            displayAddEditFragment(R.id.fragmentContainer, null);
    }

    //display a movie
    private void displayMovie(Uri movieUri, int viewID) {
        DetailFragment detailFragment = new DetailFragment();

        //specify the movie's Uri as an argument to the DetailFragment
        Bundle arguments = new Bundle();
        arguments.putParcelable(MOVIE_URI, movieUri);
        detailFragment.setArguments(arguments);

        //use a FragmentTransaction to display the DetailFragment
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit(); //causes DetailFragment to display
    }

    //display fragment for adding a new or editing an existing movie
    private void displayAddEditFragment(int viewID, Uri movieUri) {
        AddEditFragment addEditFragment = new AddEditFragment();

        //if editing existing movie, provide movieUri as an argument
        if (movieUri != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MOVIE_URI, movieUri);
            addEditFragment.setArguments(arguments);
        }

        //use a FragmentTransaction to display the AddEditFragment
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, addEditFragment);
        transaction.addToBackStack(null);
        transaction.commit(); //causes AddEditFragment to display
    }

    //return to movies list when displayed movie deleted
    @Override
    public void onMovieDeleted() {
        //removes top of back stack
        getSupportFragmentManager().popBackStack();
        moviesFragment.updateMovieList(); //refresh movies
    }

    //display the AddEditFragment to edit an existing movie
    @Override
    public void onEditMovie(Uri movieUri) {
        if(findViewById(R.id.fragmentContainer) != null)
            displayAddEditFragment(R.id.fragmentContainer, movieUri);
    }

    //update GUI after new movie or updated movie saved
    @Override
    public void onAddEditCompleted(Uri movieUri) {
        //removes top of back stack
        getSupportFragmentManager().popBackStack();
        moviesFragment.updateMovieList(); //refresh movies
    }
}
