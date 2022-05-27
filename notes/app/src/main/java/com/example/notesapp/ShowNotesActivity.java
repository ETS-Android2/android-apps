package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.notesapp.data.DatabaseHelper;
import com.example.notesapp.model.Note;
import com.example.notesapp.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ShowNotesActivity extends AppCompatActivity {

    // Variables
    ListView notesListView;
    ArrayList<String> notesArrayList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notes);

        // Assigning Variables
        notesListView = findViewById(R.id.notesListView);
        notesArrayList = new ArrayList<>();

        DatabaseHelper db = new DatabaseHelper(this);

        List<Note> notesList = db.fetchAllNotes();

        for (Note note : notesList)
        {
            notesArrayList.add(note.getNote_content());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesArrayList);
        notesListView.setAdapter(adapter);

        // When a note is pressed
        // It will pass the position value of the notes id and content
        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent editIntent = new Intent(ShowNotesActivity.this, editNoteActivity.class);

                // Pass the position of notes id and content from the selected note
                editIntent.putExtra(Util.NOTE_ID, notesList.get(position).getNote_id());
                editIntent.putExtra(Util.CONTENT, notesList.get(position).getNote_content());

                startActivityForResult(editIntent, 1);
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