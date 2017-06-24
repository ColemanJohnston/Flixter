package com.codepath.flixter;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import static com.loopj.android.http.AsyncHttpClient.log;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        String youTubeKey;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                youTubeKey = null;
            } else {
                youTubeKey = extras.getString("youTubeKey");
            }
        } else {
            youTubeKey = (String) savedInstanceState.getSerializable("youTubeKey");
        }

        final String videoId = youTubeKey;

        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

        playerView.initialize(getString(R.string.YouTube_api_key), new YouTubePlayer.OnInitializedListener(){
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b){
                youTubePlayer.cueVideo(videoId);
            }

            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult){
                log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });
    }
}
