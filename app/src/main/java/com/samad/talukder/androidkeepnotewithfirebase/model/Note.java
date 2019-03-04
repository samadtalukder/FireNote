package com.samad.talukder.androidkeepnotewithfirebase.model;

public class Note {
    private String noteId;
    private String noteTitle;
    private String noteDescription;
    private String noteDate;

    public Note() {
    }

    public Note(String noteId, String noteTitle, String noteDescription, String noteDate) {
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteDescription = noteDescription;
        this.noteDate = noteDate;
    }

    public String getNoteId() {
        return noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public String getNoteDate() {
        return noteDate;
    }
}
