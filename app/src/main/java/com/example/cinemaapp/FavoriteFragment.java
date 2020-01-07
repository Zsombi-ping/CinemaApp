package com.example.cinemaapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FavoriteFragment extends Fragment {

    private FavMovieAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ArrayList<FavMovie> favs;
    private Context context;
    private TextView title;
    private TextView description;
    DatabaseHelper db = new DatabaseHelper(getContext());


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerView = v.findViewById(R.id.favFragmentRecyclerView);
        layoutManager = new LinearLayoutManager(context);

        getfavs();

        return v;
    }


    public void getfavs() {

        SharedPreferences preferences = getContext().getSharedPreferences("CONTAINER", MODE_PRIVATE);
        String Activ_email = preferences.getString("EMAIL", "email");
        favs = db.getFavoite(Activ_email);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new FavMovieAdapter(context, favs);
        recyclerView.setAdapter(adapter);
        recyclerView.getAdapter().notifyDataSetChanged();

    }
}