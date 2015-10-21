package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class MovieDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent detailIntent = getIntent();

        MovieDataItem movieDataItem = new MovieDataItem();
        movieDataItem.setMoviePosterPath(detailIntent.getExtras().getString("movie_poster_url"));
        movieDataItem.setOriginalTitle(detailIntent.getExtras().getString("movie_original_title"));
        movieDataItem.setOverview(detailIntent.getExtras().getString("movie_overview"));
        movieDataItem.setVote_average(detailIntent.getExtras().getString("movie_vote_average"));
        movieDataItem.setReleaseDate(detailIntent.getExtras().getString("movie_release_date"));

        MovieDetailsFragment movieDetailsFragment =
                (MovieDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.movieDetailsFragment);

        movieDetailsFragment.showMovieDetails(movieDataItem);

    }
}
