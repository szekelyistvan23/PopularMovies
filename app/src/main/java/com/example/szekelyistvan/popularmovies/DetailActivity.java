/**Copyright 2018 Szekely Istvan
 *
 *        Licensed under the Apache License, Version 2.0 (the "License");
 *        you may not use this file except in compliance with the License.
 *        You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *        Unless required by applicable law or agreed to in writing, software
 *        distributed under the License is distributed on an "AS IS" BASIS,
 *        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *        See the License for the specific language governing permissions and
 *        limitations under the License
 */
package com.example.szekelyistvan.popularmovies;

/**
 * Displays data from an object received trough an intent.
 */

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.szekelyistvan.popularmovies.Adapter.MovieAdapter;
import com.example.szekelyistvan.popularmovies.Model.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    private Movie mMovieDetail;
    @BindView(R.id.user_rating) TextView mVoteAverage;
    @BindView(R.id.poster_image) ImageView mPosterPath;
    @BindView(R.id.original_title) TextView mOriginalTitle;
    @BindView(R.id.background_image) ImageView mBackdropPath;
    @BindView(R.id.overview) TextView mOverview;
    @BindView(R.id.release_date) TextView mReleaseDate;
    @BindView(R.id.action_bar_title) TextView mTextViewActionBar;
    @BindView(R.id.detailProgressBar) ProgressBar mDetailProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                mMovieDetail = extras.getParcelable(MovieAdapter.MOVIE_OBJECT);
            } else {
                finish();
                Toast.makeText(DetailActivity.this, "No data available", Toast.LENGTH_SHORT).show();
            }

            setupActionBar();
            setUpAndLoadDataToUi();


    }
    /** Sets up the views and populates it with data from a Movie object. */
    private void setUpAndLoadDataToUi(){
        ButterKnife.bind(this);
        // Picasso is distributed under Apache License, Version 2.0
        Picasso.with(this)
                .load(mMovieDetail.getPosterPath())
                .placeholder(R.drawable.blank185)
                .error(R.drawable.error185)
                .into(mPosterPath);

        Picasso.with(this)
                .load(mMovieDetail.getBackdropPath())
                .placeholder(R.drawable.blank500)
                .error(R.drawable.error500)
                .into(mBackdropPath, new Callback() {
                    @Override
                    public void onSuccess() {
                        mDetailProgressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        mDetailProgressBar.setVisibility(View.INVISIBLE);
                    }
                });

        mVoteAverage.setText(String.valueOf(mMovieDetail.getVoteAverage()));
        mOriginalTitle.setText(mMovieDetail.getOriginalTitle());
        mOverview.setText(mMovieDetail.getOverview());
        mReleaseDate.setText(mMovieDetail.getReleaseDate());

    }

    /** Sets up a custom action bar, for correct display of movies' titles. */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.action_bar_title_layout);
        }
        ButterKnife.bind(this);
        mTextViewActionBar.setText(mMovieDetail.getTitle());
    }
}
