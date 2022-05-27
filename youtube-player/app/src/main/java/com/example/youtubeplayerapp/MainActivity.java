package com.example.youtubeplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
{
    // Instancing Variables
    TextView youTubeTextView;
    EditText youTubeUrlEditText;
    Button youTubePlayButton;

    private String videoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assigning Views
        youTubeTextView = findViewById(R.id.youTubeTextView);
        youTubeUrlEditText = findViewById(R.id.youTubeUrlEditText);
        youTubePlayButton = findViewById(R.id.youTubePlayButton);

    }

    // Method handler for play button click
    public void playButtonClick(View view)
    {
        videoURL = youTubeUrlEditText.getText().toString();

        if (checkURLInput(videoURL))
        {
            videoURL = extractYoutubeID(videoURL);  // Extract the video ID from the URL

            Intent intent = new Intent(this, VideoActivity.class);
            intent.putExtra("youtubeURLKey", videoURL);
            startActivity(intent);
        }
    }


    // Method to check the URL edit text
    private boolean checkURLInput(String text)
    {
        if (text.matches(""))
        {
            Toast.makeText(this, "URL Can't be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Method to extract the video url ID manually
    // Will only work from URL of youtube.com/watch? links
    private String extractYoutubeID(String youtubeVideoURL)
    {
        String videoID = null;

        if (youtubeVideoURL.length() > 30)
        {
            videoID = youtubeVideoURL.substring(32);
            return videoID;
        }

        else throw new IllegalStateException("Unknown Youtube URL Given");
    }


}