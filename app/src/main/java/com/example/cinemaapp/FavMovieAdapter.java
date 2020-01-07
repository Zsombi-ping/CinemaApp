package com.example.cinemaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FavMovieAdapter extends RecyclerView.Adapter<FavMovieAdapter.FavMovieViewHolder> {

    private Context context;
    private List<FavMovie> movies;
    private OnBottomReachedListener onBottomReachedListener;


    public FavMovieAdapter(Context context, List<FavMovie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public FavMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_movie_list_item, parent, false);
        FavMovieViewHolder viewHolder = new FavMovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavMovieViewHolder holder, int position) {

        if (position == movies.size() - 1) {

            onBottomReachedListener.onBottomReached(position);

        }

        FavMovie movie = movies.get(position);



        holder.movieTitle.setText(movie.getTitle());
        holder.movieDescription.setText(movie.getDescript());


    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    class FavMovieViewHolder extends RecyclerView.ViewHolder {

        TextView movieTitle, movieDescription;

        public FavMovieViewHolder(@NonNull View itemView) {
            super(itemView);

            movieTitle = itemView.findViewById(R.id.favMovieTitle);
            movieDescription = itemView.findViewById(R.id.favMovieDescription);

        }
    }
}
