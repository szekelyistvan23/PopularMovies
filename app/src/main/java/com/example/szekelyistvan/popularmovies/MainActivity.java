package com.example.szekelyistvan.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

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
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Movie> moviesArray;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupRecyclerView();
        downloadData();

    }

    /** Sets up a RecyclerView */
    private void setupRecyclerView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);


    }
    /** Downloads JSON data from the Internet */
    private void downloadData(){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        String url ="https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY + "&language=en-US&page=1";

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
                            MovieAdapter movieAdapter = new MovieAdapter(MainActivity.this, moviesArray);
                            mRecyclerView.setAdapter(movieAdapter);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                finish();
                Toast.makeText(MainActivity.this,"Somenthing went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);

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
            movieResult.setVoteAverage(extractMovieData.optDouble(JSON_VOTE_AVERAGE, 0.0));
            movieResult.setTitle(extractMovieData.optString(JSON_TITLE));
            movieResult.setPosterPath(setImageSize(IMAGE_SIZE_W185, extractMovieData.optString(JSON_POSTER_PATH)));
            movieResult.setOriginalTitle(extractMovieData.optString(JSON_ORIGINAL_TITLE));
            movieResult.setBackdropPath(setImageSize(IMAGE_SIZE_W500, extractMovieData.optString(JSON_BACKDROP_PATH)));
            movieResult.setOverview(extractMovieData.optString(JSON_OVERVIEW));
            movieResult.setReleaseDate(extractMovieData.optString(JSON_RELEASE_DATE));

            resultArray.add(movieResult);
            movieResult = new Movie();
        }
        return resultArray;
    }

    /** Sets the image size for an image to be downloaded with Picasso */
    private String setImageSize (String imageSize, String imagePath){
        StringBuilder stringBuilder = new StringBuilder();

        if (imagePath!=null && imagePath !=null){
            stringBuilder.append(IMAGE_BASE_URL);
            stringBuilder.append(imageSize);
            stringBuilder.append(imagePath);
        } else {
            stringBuilder.append(NO_IMAGE);
        }
        return stringBuilder.toString();
    }
}
