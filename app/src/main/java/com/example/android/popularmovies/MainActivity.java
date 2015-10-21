package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity implements MoviesFragmentCoordinator{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onMoviePosterSelected(MovieDataItem movieDataItem) {
        MovieDetailsFragment movieDetailsFragment = (MovieDetailsFragment)getSupportFragmentManager()
                .findFragmentById(R.id.movieDetailsFragment);

        movieDetailsFragment.showMovieDetails(movieDataItem);
    }
}
