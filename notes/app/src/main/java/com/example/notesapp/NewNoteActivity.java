package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notesapp.data.DatabaseHelper;
import com.example.notesapp.model.Note;

public class NewNoteActivity extends AppCompatActivity {

    // Variables
    DatabaseHelper db;
    EditText noteEditText;
    Button saveNoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        // Assigning Variables
        noteEditText = findViewById(R.id.noteEditText);
        saveNoteButton = findViewById(R.id.saveNoteButton);
        db = new DatabaseHelper(this);


        // OnClickListener for the save note button
        saveNoteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String content = noteEditText.getText().toString();

                if (content.length() != 0)  // If the note is not empty
                {
                    long result = db.insertNote(new Note(content));

                    if (result > 0)
                    {
                        Toast.makeText(NewNoteActivity.this, "Note added successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewNoteActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    else
                    {
                        Toast.makeText(NewNoteActivity.this, "Note Creation Error!", Toast.LENGTH_SHORT).show();
                    }
                }

                else
                {
                    Toast.makeText(NewNoteActivity.this, "Note cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // To return to main menu
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}