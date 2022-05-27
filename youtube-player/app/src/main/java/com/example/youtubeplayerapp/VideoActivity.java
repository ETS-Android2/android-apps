package com.example.youtubeplayerapp;

import android.os.Bundle;

import android.content.Intent;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubeInitializationResult;

import java.security.Provider;

public class VideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    // Instancing Variables
    YouTubePlayerView youTubePlayerView;

    private String videoURL;
    private static final int RECOVERY_REQUEST = 1;  // Constant to help track if the user encountered an error

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        // Getting the URL
        Intent intent = getIntent();
        videoURL = intent.getStringExtra("youtubeURLKey");

        // Initializing the Youtube View
        youTubePlayerView = findViewById(R.id.youtubePlayerView);
        youTubePlayerView.initialize(YouTubeConfig.getApiKey(), this);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b)
    {
        youTubePlayer.cueVideo(videoURL);   // Plays the given video URL
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult)
    {
        if (youTubeInitializationResult.isUserRecoverableError())   // If user is able to recover from the error
        {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        }

        else
        {
            String errorMessage = "Error initializing Youtube player! " + youTubeInitializationResult.toString();
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == RECOVERY_REQUEST)
        {
            // Re initialize the video if recovery action is performed by the user
            youTubePlayerView.initialize(YouTubeConfig.getApiKey(), this);
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}