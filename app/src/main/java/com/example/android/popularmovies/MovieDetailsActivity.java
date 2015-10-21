package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MovieDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent detailIntent = getIntent();
        String moviePosterUrl = detailIntent.getExtras().getString("movie_poster_url");
        String movieTitle = detailIntent.getExtras().getString("movie_original_title");
        String movie_overview = detailIntent.getExtras().getString("movie_overview");
        String movie_vote_average = detailIntent.getExtras().getString("movie_vote_average");
        String movie_release_date = detailIntent.getExtras().getString("movie_release_date");

        TextView textViewTitle = (TextView)findViewById(R.id.movie_title);
        textViewTitle.setText(movieTitle);

        ImageView imageViewPoster = (ImageView)findViewById(R.id.movie_poster_image);
        Picasso.with(this).load(moviePosterUrl).into(imageViewPoster);

        TextView textViewReleaseDate = (TextView)findViewById(R.id.movie_release_date);
        textViewReleaseDate.setText(getMovieReleaseYear(movie_release_date));

        TextView textViewVoteAverage = (TextView)findViewById(R.id.movie_vote_average);
        textViewVoteAverage.setText(movie_vote_average + "/10");

        TextView textViewOverview = (TextView)findViewById(R.id.movie_overview);
        textViewOverview.setText(movie_overview);

    }

    // release date is in format : yyyy-MM-dd
    private String getMovieReleaseYear(String movieRleaseDate) {
        String[] dateParts = movieRleaseDate.split("-");
        return dateParts[0]; // this contains Year
    }
}
