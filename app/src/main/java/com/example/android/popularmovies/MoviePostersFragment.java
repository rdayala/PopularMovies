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
 * This Fragment class is used to fetch the movie posters
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
        // Add this line in order for this fragment to handle menu events.
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

        //Initialize with empty data
        mGridData = new ArrayList<>();

        // The Adapter will take data from a source and
        // use it to populate the GridView it's attached to.
        mGridAdapter = new GridViewAdapter(
                            getActivity(), // The current context (this activity)
                            R.layout.movie_poster_grid_item, // The name of the layout ID.
                            mGridData); // The data to represent in the GridView

        View rootView = inflater.inflate(R.layout.movie_posters_fragment, container, false);

        // Get a reference to the GridView, and attach this adapter to it.
        mGridView = (GridView)rootView.findViewById(R.id.movie_posters_grid);
        mGridView.setAdapter(mGridAdapter);

        // On Click event for Gridview Item
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Fragment movieDetailsFragment = getActivity().getSupportFragmentManager()
                        .findFragmentById(R.id.movieDetailsFragment);

                // passing array index
                MovieDataItem item = mGridData.get(position);

                if (movieDetailsFragment != null && movieDetailsFragment.isVisible()) {
                    ((MoviesFragmentCoordinator)getActivity()).onMoviePosterSelected(item);
                }
                else {
                    Intent movieDetailIntent = new Intent(getActivity(), MovieDetailsActivity.class);
                    movieDetailIntent.putExtra(getString(R.string.movie_id), item.getMovieId());
                    movieDetailIntent.putExtra(getString(R.string.movie_poster_url), item.getMoviePosterPathUrl());
                    movieDetailIntent.putExtra(getString(R.string.movie_original_title), item.getOriginalTitle());
                    movieDetailIntent.putExtra(getString(R.string.movie_overview), item.getOverview());
                    movieDetailIntent.putExtra(getString(R.string.movie_vote_average), item.getVote_average());
                    movieDetailIntent.putExtra(getString(R.string.movie_release_date), item.getReleaseDate());
                    startActivity(movieDetailIntent);
                }
            }
        });

        if(savedInstanceState != null) {
            moviesJsonData = savedInstanceState.getString(getString(R.string.movie_saved_json_data));
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
                // during initial launch, show the first item detail
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

        // These are the names of the JSON objects that need to be extracted.
        final String MDB_RESULTS = "results";
        final String MDB_MOVIE_ID = "id";
        final String MDB_MOVIE_POSTER_PATH = "poster_path";
        final String MDB_MOVIE_ORIGINAL_TITLE = "original_title";
        final String MDB_MOVIE_OVERVIEW = "overview";
        final String MDB_MOVIE_VOTE_AVERAGE = "vote_average";
        final String MDB_MOVIE_RELEASE_DATE = "release_date";
        final String MDB_MOVIE_ORIG_LANG = "original_language";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(MDB_RESULTS);

        List<MovieDataItem> movieItems = new ArrayList<MovieDataItem>();
        for(int i = 0; i < moviesArray.length(); i++) {

            // Get the JSON object representing the movie
            JSONObject movieInfo = moviesArray.getJSONObject(i);

            String movieId = movieInfo.getString(MDB_MOVIE_ID);
            String imageUrl = movieInfo.getString(MDB_MOVIE_POSTER_PATH);
            String originalTitle = movieInfo.getString(MDB_MOVIE_ORIGINAL_TITLE);
            String movieOverview = movieInfo.getString(MDB_MOVIE_OVERVIEW);
            String vote_average = movieInfo.getString(MDB_MOVIE_VOTE_AVERAGE);
            String releaseDate = movieInfo.getString(MDB_MOVIE_RELEASE_DATE);
            String origLanguage = movieInfo.getString(MDB_MOVIE_ORIG_LANG);

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
        fetchMoviesTask.execute(sortChoice);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.movie_saved_json_data), moviesJsonData);
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
                final String API_KEY = "XXXXXXXXXXXXXXXXXXXXXXXX"; // API key to be used with themoviedb.org APIs
                String sortChoice = params[0];

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, sortChoice)
                        .appendQueryParameter(API_KEY_PARAM, API_KEY)
                        .build();

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
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the movie data.
            return null;
        }

        @Override
        protected void onPostExecute(Object[] movieItems) {
            updateMoviePostersFragment(movieItems);
        }
    }
}