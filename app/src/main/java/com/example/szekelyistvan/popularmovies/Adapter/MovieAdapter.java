package com.example.szekelyistvan.popularmovies.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.szekelyistvan.popularmovies.DetailActivity;
import com.example.szekelyistvan.popularmovies.Model.Movie;
import com.example.szekelyistvan.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Szekely Istvan on 22.02.2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context mContext;
    private List<Movie> mMovies;
    public static final String MOVIE_OBJECT = "movie_object";

    public MovieAdapter(Context context, List<Movie> movies) {
        mContext = context;
        mMovies = movies;
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.movie_thumbnail, parent, false);
        return new MovieAdapter.MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder, int position) {
        Picasso.with(mContext)
                .load(mMovies.get(position).getPosterPath())
                .placeholder(R.drawable.blank185)
                .error(R.drawable.error185)
                .into(holder.mMoviePoster);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void changeMovieData(List<Movie> newMovies) {
        mMovies = newMovies;
        notifyDataSetChanged();
    }
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.movie_thumbnail_image) ImageView mMoviePoster;
        public MovieViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onClick(View v) {
            Bundle args = new Bundle();
            int position = getAdapterPosition();
            args.putParcelable(MOVIE_OBJECT, mMovies.get(position));
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtras(args);
            mContext.startActivity(intent);
        }
    }
}
