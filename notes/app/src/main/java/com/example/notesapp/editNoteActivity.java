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
import com.example.notesapp.util.Util;

public class editNoteActivity extends AppCompatActivity {

    // Variables
    EditText noteContentEditText;
    Button updateNoteButton, deleteNoteButton;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        // Assigning Variables
        noteContentEditText = findViewById(R.id.noteContentEditText);
        updateNoteButton = findViewById(R.id.updateNoteButton);
        deleteNoteButton = findViewById(R.id.deleteNoteButton);

        db = new DatabaseHelper(this);

        Intent getEditIntent = getIntent();
        String noteContent = getEditIntent.getStringExtra(Util.CONTENT);
        int noteID = getEditIntent.getIntExtra(Util.NOTE_ID, 0);

        noteContentEditText.setText(noteContent);   // Set the note content

        // OnClickListener for update button
        updateNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String content = noteContentEditText.getText().toString();
                int id = noteID;

                int updateRow = db.updateNote(new Note(id, content));

                if (updateRow > 0)
                {
                    Toast.makeText(editNoteActivity.this, "Note edited successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(editNoteActivity.this, ShowNotesActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(editNoteActivity.this, "Note Update Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // OnClickListener for delete button
        deleteNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = noteContentEditText.getText().toString();
                int id = noteID;

                int deleteRow = db.deleteNote(new Note(id, content));

                if (deleteRow > 0)
                {
                    Toast.makeText(editNoteActivity.this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(editNoteActivity.this, ShowNotesActivity.class);
                    startActivity(intent);
                    finish();
                }

                else
                {
                    Toast.makeText(editNoteActivity.this, "Note deletion failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // To return to show notes
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ShowNotesActivity.class);
        startActivity(intent);
        finish();
    }
}