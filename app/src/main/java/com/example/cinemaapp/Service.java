package com.example.cinemaapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Service {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String api_key, @Query("page") int pageIndex);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String api_key, @Query("page") int pageIndex);

    @GET("search/movie")
    Call<MovieResponse> getSearch(@Query("api_key") String api_key,@Query("query") String query,@Query("page") int pageIndex);

}