/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.data.source.remote;
// Created by ahmedsomer on 4/3/20.
//
///***SeNote
//
//

import androidx.lifecycle.LiveData;

import com.aso.senote.data.models.Note;
import com.aso.senote.data.models.Notebook;
import com.aso.senote.data.source.NotesDataSource;

import java.util.List;

public class RemoteNotesDataSource implements NotesDataSource {

    public RemoteNotesDataSource() {
    }

    @Override
    public LiveData<List<Notebook>> observeNotebooks() {
        return null;
    }

    @Override
    public LiveData<List<Note>> observeNotes() {
        return null;
    }

    @Override
    public LiveData<List<Note>> observeNotesForNotebook(long notebookId) {
        return null;
    }

    @Override
    public LiveData<Note> observeNoteById(long noteId) {
        return null;
    }

    @Override
    public void saveNote(Note note) {

    }

    @Override
    public void saveNotebook(Notebook notebook) {

    }

    @Override
    public void deleteNotes(Note... notes) {

    }

    @Override
    public void deleteNotebooks(Notebook... notebooks) {

    }

    @Override
    public void deleteAllNotes() {

    }

    @Override
    public void deleteAllNotebooks() {

    }
}
