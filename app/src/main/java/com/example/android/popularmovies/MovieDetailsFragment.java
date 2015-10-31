package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by rdayala on 10/21/2015.
 */
public class MovieDetailsFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_details_fragment, container, false);
        return rootView;
    }

    public void showMovieDetails(MovieDataItem movieDataItem) {
        final String fullVotes = "10";

        TextView textViewTitle = (TextView)getActivity().findViewById(R.id.movie_title);
        textViewTitle.setText(movieDataItem.getOriginalTitle());

        ImageView imageViewPoster = (ImageView)getActivity().findViewById(R.id.movie_poster_image);
        Picasso.with(getActivity()).load(movieDataItem.getMoviePosterPathUrl()).into(imageViewPoster);

        TextView textViewReleaseDate = (TextView)getActivity().findViewById(R.id.movie_release_date);
        textViewReleaseDate.setText(getMovieReleaseYear(movieDataItem.getReleaseDate()));

        TextView textViewVoteAverage = (TextView)getActivity().findViewById(R.id.movie_vote_average);
        textViewVoteAverage.setText(movieDataItem.getVote_average() + "/" +  fullVotes);

        TextView textViewOverview = (TextView)getActivity().findViewById(R.id.movie_overview);
        textViewOverview.setText(movieDataItem.getOverview());
    }

    // release date is in format : yyyy-MM-dd
    private String getMovieReleaseYear(String movieRleaseDate) {
        String[] dateParts = movieRleaseDate.split("-");
        return dateParts[0]; // this contains Year
    }
}
