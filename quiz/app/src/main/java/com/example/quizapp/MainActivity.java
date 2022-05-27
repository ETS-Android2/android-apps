package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText StartEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartEditText = findViewById(R.id.StartEditText1);
    }

    // Function to jump activity
    public void jumpClick(View view)
    {
        String userName = StartEditText.getText().toString();

        if (userName.matches(""))
        {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
        }

        else
        {
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("username", StartEditText.getText().toString());
            startActivity(intent);
            finish();   // Closes the main activity
        }

    }
}