package com.example.notesapp.model;

public class Note
{
    // Variables
    private int note_id;
    private String note_content;

    // Constructor
    public Note(int note_id, String note_content) {
        this.note_id = note_id;
        this.note_content = note_content;
    }


    public Note(String note_content) {
        this.note_content = note_content;
    }

    public Note() {
    }

    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public String getNote_content() {
        return note_content;
    }

    public void setNote_content(String note_content) {
        this.note_content = note_content;
    }
}
