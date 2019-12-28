package com.example.cinemaapp;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public int PAGE = 1;
    public static String API_KEY = "55d2a7718c60c04a3dcdb349b63c06e6";
    public static String LANGUAGE = "en-US";
    public static String CATEGORY = "popular";

    private Context context;
    private TopMoviesAdapter adapter;
    private List<MovieResult> movies = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SearchView searchView;
    private String queryString = "";

    public HomeFragment(Context context) {
        this.context = context;
    }

    public HomeFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.homeFragmentRecyclerView);
        layoutManager = new LinearLayoutManager(context);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryString = query;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryString = newText;

                if (newText.isEmpty()) {
                    initMovieList();
                } else {
                    search(newText);
                }

                return false;
            }
        });


        if (movies.isEmpty() && queryString.isEmpty()) {
            initMovieList();
        }


        return view;
    }

    private void initMovieList() {


        Call<MovieResponse> call = apiInterface().getPopularMovies(API_KEY, PAGE);


        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse responseBody = response.body();
                try {
                    List<MovieResult> movieList = responseBody.getMovieResults();
//                    Toast.makeText(getContext(), movieList.get(0).getTitle(), Toast.LENGTH_SHORT).show();
                    movies = new ArrayList<>();
                    movies.addAll(movieList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new TopMoviesAdapter(context, movies, getFragmentManager());
                    adapter.setOnBottomReachedListener(position -> loadMoreData(++PAGE));
                    recyclerView.setAdapter(adapter);
                    recyclerView.getAdapter().notifyDataSetChanged();

                } catch (Exception e) {
//                    Toast.makeText(getContext(), "Something went wrong:" + e.toString(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("HomeFragmentResponse", "Failed api call!");

            }
        });

    }

    private void search(String searchString) {

        Call<MovieResponse> call = apiInterface().getSearch(API_KEY, searchString,1);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.body().getTotalResults() > 0) {
                    List<MovieResult> movieList = response.body().getMovieResults();
                    movies = new ArrayList<>();
                    movies.addAll(movieList);

                    recyclerView.getAdapter().notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }


    private void loadMoreData(int pageNumber) {

        Call<MovieResponse> call = apiInterface().getPopularMovies(API_KEY, pageNumber);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<MovieResult> movieList = response.body().getMovieResults();
                movies.addAll(movieList);

                recyclerView.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private Service apiInterface() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Service apiService = retrofit.create(Service.class);

        return apiService;
    }
}