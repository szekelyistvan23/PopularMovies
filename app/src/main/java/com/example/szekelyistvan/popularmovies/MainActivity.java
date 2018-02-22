package com.example.szekelyistvan.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    /* Put your own api key from themoviedb.org to gradle.properties in the following form
    API_KEY = "your api key goes here" */
    private static final String API_KEY = BuildConfig.API_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
