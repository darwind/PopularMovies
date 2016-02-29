package it.rugaard.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import it.rugaard.popularmovies.models.Movie;
import it.rugaard.popularmovies.models.MovieImage;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.MovieViewHolder> {
    public interface OnMovieClickListener {
        void onClick(View view, Movie movie);
    }

    private List<Movie> movies;
    private OnMovieClickListener onMovieClickListener;

    public MoviesRecyclerAdapter(OnMovieClickListener onMovieClickListener) {
        this.onMovieClickListener = onMovieClickListener;
        this.movies = new ArrayList<>();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        return new MovieViewHolder(view, onMovieClickListener);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.setModel(movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void updateData(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView posterImageView;
        private OnMovieClickListener onMovieClickListener;
        private Movie movie;

        public MovieViewHolder(View view, OnMovieClickListener onMovieClickListener) {
            super(view);
            posterImageView = (ImageView) view;
            this.onMovieClickListener = onMovieClickListener;
            this.itemView.setOnClickListener(this);
        }

        public void setModel(Movie movie) {
            this.movie = movie;
            Glide.with(itemView.getContext())
                    .load(new MovieImage(movie.getBackdropPath()).toUri())
                    .placeholder(R.drawable.ic_videocam_black_24dp)
                    .into(posterImageView);
        }

        @Override
        public void onClick(View v) {
            onMovieClickListener.onClick(v, movie);
        }
    }
}