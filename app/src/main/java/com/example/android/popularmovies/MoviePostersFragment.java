package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * This Fragment class is used to fetch the movie posters.
 *
 */
public class MoviePostersFragment extends Fragment {

    private static final String LOG_TAG = MoviePostersFragment.class.getSimpleName();

    private GridView mGridView;
    private GridViewAdapter mGridAdapter;
    private ArrayList<MovieDataItem> mGridData;

    private String moviesJsonData;

    public MoviePostersFragment() {    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular_movies) {
            fetchMoviesAndUpdateView(getString(R.string.popular_movies_chocie));
            return true;
        }
        else if(id == R.id.action_high_rated) {
            fetchMoviesAndUpdateView(getString(R.string.high_rated_movies_choice));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_posters_fragment, container, false);
        mGridView = (GridView)rootView.findViewById(R.id.movie_posters_grid);

        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(getActivity(), R.layout.movie_poster_grid_item, mGridData);
        mGridView.setAdapter(mGridAdapter);

        /**
         * On Click event for Single Gridview Item
         * */
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Fragment movieDetailsFragment = getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.movieDetailsFragment);

                if (movieDetailsFragment != null && movieDetailsFragment.isVisible()) {
                    // passing array index
                    MovieDataItem item = mGridData.get(position);
                    ((MoviesFragmentCoordinator)getActivity()).onMoviePosterSelected(item);
                }
                else {
                    Intent movieDetailIntent = new Intent(getActivity().getApplicationContext(), MovieDetailsActivity.class);
                    // passing array index
                    MovieDataItem item = mGridData.get(position);

                    movieDetailIntent.putExtra("movie_id", item.getMovieId());
                    movieDetailIntent.putExtra("movie_poster_url", item.getMoviePosterPathUrl());
                    movieDetailIntent.putExtra("movie_original_title", item.getOriginalTitle());
                    movieDetailIntent.putExtra("movie_overview", item.getOverview());
                    movieDetailIntent.putExtra("movie_vote_average", item.getVote_average());
                    movieDetailIntent.putExtra("movie_release_date", item.getReleaseDate());
                    startActivity(movieDetailIntent);
                }
            }
        });

        if(savedInstanceState != null) {
            moviesJsonData = savedInstanceState.getString("moviesJsonData");
        }

        return rootView;
    }

    @Override
    public void onResume() {
        // The activity has become visible (it is now "resumed").
        super.onResume();

        if(moviesJsonData != null) {
            // the fragment already exists from previous state
            // just update the UI, don't fetch data
            try {
                updateMoviePostersFragment(getMovieDataFromJson(moviesJsonData));

            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        else {
            // the fragment is visible for the first time
            // fetch the data and update the view
            // on App launch, by default show popular movies
            fetchMoviesAndUpdateView(getString(R.string.popular_movies_chocie));
        }
    }

    public void updateMoviePostersFragment(Object[] movieItems) {
        // Download complete. Let us update UI
        if (movieItems != null) {
            mGridAdapter.clear();
            for(Object movieItem : movieItems)
            {   MovieDataItem item = (MovieDataItem)movieItem;
                mGridData.add(item);
            }
            mGridAdapter.setGridData(mGridData);

            Fragment movieDetailsFragment = getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.movieDetailsFragment);

            // if the orientation is landscape and movie details fragment
            // is available and visible, show the details
            // on App launch, by default show the first movie details
            if (movieDetailsFragment != null && movieDetailsFragment.isInLayout()) {
                // passing array index
                MovieDataItem item = mGridData.get(0);
                ((MoviesFragmentCoordinator)getActivity()).onMoviePosterSelected(item);
            }
        }
    }

    /**
     * Take the String representing the complete data in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private Object[] getMovieDataFromJson(String moviesJsonStr)
            throws JSONException {

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray("results");

        List<MovieDataItem> movieItems = new ArrayList<MovieDataItem>();
        for(int i = 0; i < moviesArray.length(); i++) {

            // Get the JSON object representing the movie
            JSONObject movieInfo = moviesArray.getJSONObject(i);

            String movieId = movieInfo.getString("id");
            String imageUrl = movieInfo.getString("poster_path");
            String originalTitle = movieInfo.getString("original_title");
            String movieOverview = movieInfo.getString("overview");
            String vote_average = movieInfo.getString("vote_average");
            String releaseDate = movieInfo.getString("release_date");
            String origLanguage = movieInfo.getString("original_language");

            MovieDataItem item = new MovieDataItem();
            item.setMoviePosterPath(imageUrl);
            item.setOriginalTitle(originalTitle);
            item.setOverview(movieOverview);
            item.setVote_average(vote_average);
            item.setReleaseDate(releaseDate);
            item.setOrigLanguage(origLanguage);

            movieItems.add(item);
        }

        return movieItems.toArray();

    }

    private void fetchMoviesAndUpdateView(String sortChoice) {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        if((sortChoice == null) || (sortChoice == "")) {
            sortChoice = getString(R.string.popular_movies_chocie);
        }
        fetchMoviesTask.execute(getString(R.string.api_key), sortChoice);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(LOG_TAG, "onSaveInstanceState called!");
        super.onSaveInstanceState(outState);
        outState.putString("moviesJsonData", moviesJsonData);
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, Object[]> {

        @Override
        protected Object[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                // Construct the URL for the themoviedb query
                // URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=[YOUR API KEY]");
                final String MOVIES_BASE_URL ="http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";
                String apiKey = params[0];
                String sortChoice = params[1];

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, sortChoice)
                        .appendQueryParameter(API_KEY_PARAM, apiKey)
                        .build();

                Log.v(LOG_TAG, builtUri.toString());
                URL url = new URL(builtUri.toString());

                // Create the request to themoviedb.org API, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonData = buffer.toString();
                Log.v(LOG_TAG, "Popular Movies JSON string: " + moviesJsonData);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movie data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(moviesJsonData);

            }catch (JSONException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object[] movieItems) {
            updateMoviePostersFragment(movieItems);
        }
    }
}