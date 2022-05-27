package com.example.notesapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.notesapp.model.Note;
import com.example.notesapp.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper
{

    public DatabaseHelper(@Nullable Context context)
    {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "(" + Util.NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + Util.CONTENT + " TEXT)";   // Creates a table with 2 value, noteID and Content

        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String DROP_NOTES_TABLE = "DROP TABLE IF EXISTS";   // Will deletethe table if it exists
        db.execSQL(DROP_NOTES_TABLE, new String[]{Util.TABLE_NAME});

        onCreate(db);
    }

    // Method to insert a new note into the database
    public long insertNote(Note note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.CONTENT, note.getNote_content());

        long newRowID = db.insert(Util.TABLE_NAME, null, contentValues);
        db.close();

        return newRowID;
    }

    // Method to fetch a note
    public boolean fetchNote(String content)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TABLE_NAME, new String[]{Util.NOTE_ID}, Util.CONTENT + "=?",
                new String[]{content}, null, null, null);

        int numberOfRows = cursor.getCount();
        db.close();

        if (numberOfRows > 0) return true;

        else return false;
    }

    // Method to fetch all notes
    public List<Note> fetchAllNotes()
    {
        List<Note> noteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectAll = " SELECT * FROM " + Util.TABLE_NAME; // Select all from notes table
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst())
        {
            do {
                Note note = new Note();
                note.setNote_id(cursor.getInt(0));
                note.setNote_content(cursor.getString(1));

                noteList.add(note);
            } while (cursor.moveToNext());
        }
        return noteList;
    }

    // Method to update a note
    public int updateNote(Note note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Util.NOTE_ID, note.getNote_id());
        contentValues.put(Util.CONTENT, note.getNote_content());

        return db.update(Util.TABLE_NAME, contentValues, Util.NOTE_ID + "=?", new String[]{String.valueOf(note.getNote_id())});
    }


    // Method to delete a note
    public int deleteNote(Note note)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Util.TABLE_NAME, Util.NOTE_ID + "=?", new String[]{String.valueOf(note.getNote_id())});
    }
}
