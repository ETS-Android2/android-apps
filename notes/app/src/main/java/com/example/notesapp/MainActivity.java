package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // Declaring Variables
    Button createButton, showButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // On button click
    public void buttonClick(View view)
    {
        switch (view.getId())
        {
            case R.id.createButton:
                Intent newIntent = new Intent(this, NewNoteActivity.class);
                startActivity(newIntent);
                break;

            case R.id.showButton:
                Intent showIntent = new Intent(this, ShowNotesActivity.class);
                startActivity(showIntent);
                break;

            default:
                throw new IllegalStateException("Unexpected Value" + view.getId());
        }
    }
}