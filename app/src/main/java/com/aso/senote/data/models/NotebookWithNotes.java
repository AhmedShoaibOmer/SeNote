/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.data.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NotebookWithNotes {
    @Embedded
    private Notebook notebook;
    @Relation(
            parentColumn = "notebookId",
            entityColumn = "notebookId"
    )
    private List<Note> notes;

    public NotebookWithNotes(Notebook notebook, List<Note> notes) {
        this.notebook = notebook;
        this.notes = notes;
    }

    public Notebook getNotebook() {
        return notebook;
    }

    public void setNotebook(Notebook notebook) {
        this.notebook = notebook;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    @NotNull
    @Override
    public String toString() {
        return "NotebookWithNotes{" +
                "notebook=" + notebook +
                ", notes=" + notes +
                '}';
    }
}
