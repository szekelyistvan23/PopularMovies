package com.example.szekelyistvan.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.szekelyistvan.popularmovies.Model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Szekely Istvan on 22.02.2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context mContext;
    private List<Movie> mMovies;

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
                .into(holder.mMoviePoster);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView mMoviePoster;
        public MovieViewHolder(View itemView) {
            super(itemView);
            mMoviePoster = itemView.findViewById(R.id.movie_thumbnail_image);
        }
    }
}
