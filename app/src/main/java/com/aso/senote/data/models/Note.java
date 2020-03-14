/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "note_table",
        indices = {@Index(value = "noteId"), @Index(value = "notebookId")},
        foreignKeys = @ForeignKey(entity = Notebook.class,
                parentColumns = "notebookId",
                childColumns = "notebookId",
                onDelete = ForeignKey.CASCADE)
)
public class Note {


    @ColumnInfo(name = "date_created")
    private Calendar dateCreated;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "noteId")
    private long id;
    private long notebookId;
    private String noteTitle;
    private String noteContent;
    private Calendar lastModified;

    public Note(long notebookId, String noteTitle, String noteContent) {
        this.dateCreated = Calendar.getInstance();
        this.notebookId = notebookId;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.lastModified = Calendar.getInstance();
    }

    public long getNotebookId() {
        return notebookId;
    }

    public void setNotebookId(long notebookId) {
        this.notebookId = notebookId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Calendar dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Calendar getLastModified() {
        return lastModified;
    }

    public void setLastModified(Calendar lastModified) {
        this.lastModified = lastModified;
    }
}
