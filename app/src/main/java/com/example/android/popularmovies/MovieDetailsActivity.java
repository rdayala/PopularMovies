package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

        TextView textView = (TextView)findViewById(R.id.movie_title);
        textView.setText(movieTitle);

        ImageView imageView = (ImageView)findViewById(R.id.movie_poster_image);
        Picasso.with(this).load(moviePosterUrl).into(imageView);


        TextView textView2 = (TextView)findViewById(R.id.movie_title2);
        textView2.setText(movieTitle);

        TextView textView3 = (TextView)findViewById(R.id.movie_vote_average);
        textView3.setText(movie_vote_average + "/10");


        TextView textView4 = (TextView)findViewById(R.id.movie_overview);
        textView4.setText(movie_overview);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
