package com.example.android.popularmovies;

/**
 * Created by rdayala on 10/8/2015.
 * This class contains the Movie Data
 */
public class MovieDataItem {
    private static final String MOVIE_POSTER_URL = "http://image.tmdb.org/t/p/w185/";

    private String movieId;         // id
    private String moviePosterPath; // poster_path
    private String originalTitle;   // original_title
    private String overview;        // overview
    private String vote_average;    // vote_average
    private String releaseDate;     // release_date
    private String origLanguage;    // original_language

    public MovieDataItem() {

    }

    // Constructor to initialize minimum data for a Movie
    public MovieDataItem(String movieId, String moviePosterPath, String originalTitle) {
        this.movieId = movieId;
        this.moviePosterPath = moviePosterPath;
        this.originalTitle = originalTitle;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMoviePosterPath() {
        return moviePosterPath;
    }

    public void setMoviePosterPath(String moviePosterPath) {
        this.moviePosterPath = moviePosterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOrigLanguage() {
        return origLanguage;
    }

    public void setOrigLanguage(String origLanguage) {
        this.origLanguage = origLanguage;
    }

    public String getMoviePosterPathUrl() {
        return MOVIE_POSTER_URL + getMoviePosterPath();
    }
}