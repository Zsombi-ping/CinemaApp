package com.example.cinemaapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailDialogFragment extends DialogFragment {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public int PAGE = 1;
    public static String API_KEY = "55d2a7718c60c04a3dcdb349b63c06e6";

    private MovieResult movie;
    private Context context;
    private TextView title;
    private TextView description;
    private ImageView close;
    private ImageView favoriteMovie;


    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private Boolean isFavorite = false;


    private VideoResult videoResult;

    public DetailDialogFragment() {
    }

    public DetailDialogFragment(MovieResult movieResult, Context context) {
        this.context = context;
        this.movie = movieResult;
        getVideo();

    }


    static DetailDialogFragment newInstance() {
        return new DetailDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.detail_fragment_layout, container, false);
        YouTubePlayerView youtubePlayerView = view.findViewById(R.id.youtubePlayerView);
        title = view.findViewById(R.id.detailTitle);
        description = view.findViewById(R.id.detailDescription);
        close = view.findViewById(R.id.dialogX);
        favoriteMovie = view.findViewById(R.id.favoriteMovie);
        close.setOnClickListener(v -> dismiss());
        title.setText(movie.getTitle());
        description.setText(movie.getOverview());

        favoriteMovie.setImageResource(R.drawable.ic_heart_full);


        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "S0Q4gqBUs7c";
                if (videoResult != null) {
                    youTubePlayer.loadVideo(videoResult.getKey().isEmpty() ? videoId : videoResult.getKey(), 0);
                }
            }
        });

        return view;
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

    private void getVideo() {

        Call<VideoResponse> call = apiInterface().getVideo(movie.getId(), API_KEY);

        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                VideoResponse responseBody = response.body();
                try {
                    List<VideoResult> videoList = responseBody.getResults();
                    videoResult = videoList.get(0);


                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                Log.d("HomeFragmentResponse", "Failed api call!");

            }
        });
    }
}
