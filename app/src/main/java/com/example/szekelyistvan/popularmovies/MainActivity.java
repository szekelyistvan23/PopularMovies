package com.example.szekelyistvan.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.szekelyistvan.popularmovies.Adapter.MovieAdapter;
import com.example.szekelyistvan.popularmovies.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    /* Put your own api key from themoviedb.org to gradle.properties in the following form
    API_KEY = "your api key goes here" */
    private static final String API_KEY = BuildConfig.API_KEY;
    public static final String JSON_ARRAY_RESULTS = "results";
    public static final String JSON_ID = "id";
    public static final String JSON_VOTE_AVERAGE = "vote_average";
    public static final String JSON_TITLE = "title";
    public static final String JSON_POSTER_PATH = "poster_path";
    public static final String JSON_ORIGINAL_TITLE = "original_title";
    public static final String JSON_BACKDROP_PATH = "backdrop_path";
    public static final String JSON_OVERVIEW = "overview";
    public static final String JSON_RELEASE_DATE = "release_date";
    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE_W185 = "w185/";
    public static final String IMAGE_SIZE_W500 = "w500/";
    public static final String NO_IMAGE = "NO IMAGE";
    public static final String JSON_REQUEST_BASE_URL = "https://api.themoviedb.org/3/movie";
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String API_KEY_QUERY = "api_key";
    public static final String LANGUAGE_QUERY = "language";
    public static final String LANGUAGE_QUERY_VALUE = "en-US";
    public static final String PAGE_QUERY = "page";
    public static final String PAGE_QUERY_VALUE = "1";
    public static final double AVERAGE_VOTE_FALLBACK = 0.0;
    @BindView(R.id.recycler_view_main) RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Movie> moviesArray;
    private @MovieListType String defaultQuery =POPULAR;
    private Toast sortToast;

    @StringDef({POPULAR, TOP_RATED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MovieListType {
    }

    @StringDef({IMAGE_SIZE_W185, IMAGE_SIZE_W185})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MovieImageSize {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupRecyclerView();
        if (haveNetworkConnection()) {
            downloadData(defaultQuery);
        } else {
            finish();
            showToast(getString(R.string.no_internet));
        }
    }

    /** Sets up a RecyclerView */
    private void setupRecyclerView(){
        ButterKnife.bind(this);
        mRecyclerView.setHasFixedSize(true);

        //TODO Fix grid layout
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);


    }
    /** Downloads JSON data from the Internet */
    private void downloadData(@MovieListType String query){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        String url = buildStringForRequest(query);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            moviesArray = jsonToMovieArray(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (moviesArray != null && !moviesArray.isEmpty()){
                            mAdapter = new MovieAdapter(MainActivity.this, moviesArray);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
                Toast.makeText(MainActivity.this, R.string.wrong, Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortPopular:
                if (haveNetworkConnection()) {
                    sortPopular();
                } else {
                    showToast(getString(R.string.no_internet));
                }
                return true;
            case R.id.sortTopRated:
                if (haveNetworkConnection()) {
                    sortTopRated();
                } else {
                    showToast(getString(R.string.no_internet));
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        }

    /** Builds a String for an Internet request based on the users request. */
    private String buildStringForRequest(@MovieListType String queryType){

        if (queryType != null) {
            Uri uri = Uri.parse(JSON_REQUEST_BASE_URL).buildUpon()
                    .appendPath(queryType)
                    .appendQueryParameter(API_KEY_QUERY, API_KEY)
                    .appendQueryParameter(LANGUAGE_QUERY, LANGUAGE_QUERY_VALUE)
                    .appendQueryParameter(PAGE_QUERY, PAGE_QUERY_VALUE)
                    .build();
            return uri.toString();
        } else {
            return "";
        }
    }

    /** Checks the state of the network connection. */
    public boolean haveNetworkConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected()) {
                    return true;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnected()) {
                    return true;
            }
        }
        return false;
    }

    /** Displays the popular movies list. */
    private void sortPopular(){
        if (defaultQuery.equals(POPULAR)) {
            showToast(getString(R.string.already_popular));
        } else {
            defaultQuery = POPULAR;
            downloadData(defaultQuery);
            mAdapter.changeMovieData(moviesArray);
            setTitle(getString(R.string.popular_movies_title));
        }
    }

    /** Displays the top rated movies list. */
    private void sortTopRated(){
        if (defaultQuery.equals(TOP_RATED)) {
            showToast(getString(R.string.already_top));
        } else {
            defaultQuery = TOP_RATED;
            downloadData(defaultQuery);
            mAdapter.changeMovieData(moviesArray);
            setTitle(getString(R.string.highest_rated_title));
        }
    }

    /** Displays a toast message after canceling the previous one. */
    public void showToast(String text){
        if (sortToast != null){
            sortToast.cancel();
        }
        sortToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        sortToast.show();
    }

    /** Parses JSONArroy to a Movie Array */
    private List<Movie> jsonToMovieArray(String jsonResponse) throws JSONException{
        List<Movie> resultArray = new ArrayList<>();
        Movie movieResult = new Movie();
        JSONObject extractMovieData;

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray jsonArray = jsonObject.getJSONArray(JSON_ARRAY_RESULTS);

        for (int i=0; i < jsonArray.length(); i++){
            extractMovieData = (JSONObject) jsonArray.get(i);

            movieResult.setId(extractMovieData.optInt(JSON_ID));
            movieResult.setVoteAverage(extractMovieData.optDouble(JSON_VOTE_AVERAGE, AVERAGE_VOTE_FALLBACK));
            movieResult.setTitle(extractMovieData.optString(JSON_TITLE));
            movieResult.setPosterPath(setImageSize(IMAGE_SIZE_W185, extractMovieData.optString(JSON_POSTER_PATH)));
            movieResult.setOriginalTitle(extractMovieData.optString(JSON_ORIGINAL_TITLE));
            movieResult.setBackdropPath(setImageSize((String)IMAGE_SIZE_W500, extractMovieData.optString(JSON_BACKDROP_PATH)));
            movieResult.setOverview(extractMovieData.optString(JSON_OVERVIEW));
            movieResult.setReleaseDate(extractMovieData.optString(JSON_RELEASE_DATE));

            resultArray.add(movieResult);
            movieResult = new Movie();
        }
        return resultArray;
    }

    /** Sets the image size for an image to be downloaded with Picasso */
    private String setImageSize (@MovieImageSize String imageSize, String imagePath){
        StringBuilder stringBuilder = new StringBuilder();

        if (imageSize!=null && imagePath !=null){
            stringBuilder.append(IMAGE_BASE_URL);
            stringBuilder.append(imageSize);
            stringBuilder.append(imagePath);
        } else {
            stringBuilder.append(NO_IMAGE);
        }
        return stringBuilder.toString();
    }
}
