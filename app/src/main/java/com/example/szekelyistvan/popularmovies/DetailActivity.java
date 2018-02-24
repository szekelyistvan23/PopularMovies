package com.example.szekelyistvan.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.szekelyistvan.popularmovies.Adapter.MovieAdapter;
import com.example.szekelyistvan.popularmovies.Model.Movie;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {
    private Movie mMovieDetail;

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

            setUpAndLoadDataToUI();


    }
    /** Sets up the views and populates with data from an Movie object. */
    private void setUpAndLoadDataToUI(){
        TextView mVoteAverage;
        ImageView mPosterPath;
        TextView mOriginalTitle;
        ImageView mBackdropPath;
        TextView mOverview;
        TextView mReleaseDate;

        setTitle(mMovieDetail.getTitle());

        mVoteAverage = findViewById(R.id.user_rating);
        mPosterPath = findViewById(R.id.poster_image);
        mOriginalTitle = findViewById(R.id.original_title);
        mBackdropPath = findViewById(R.id.background_image);
        mOverview = findViewById(R.id.overview);
        mReleaseDate = findViewById(R.id.release_date);

        Picasso.with(this)
                .load(mMovieDetail.getPosterPath())
                .placeholder(R.drawable.blank185)
                .into(mPosterPath);

        Picasso.with(this)
                .load(mMovieDetail.getBackdropPath())
                .placeholder(R.drawable.blank500)
                .into(mBackdropPath);

        mVoteAverage.setText(String.valueOf(mMovieDetail.getVoteAverage()));
        mOriginalTitle.setText(mMovieDetail.getOriginalTitle());
        mOverview.setText(mMovieDetail.getOverview());
        mReleaseDate.setText(mMovieDetail.getReleaseDate());

    }
}
