/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.data.source;
// Created by ahmedsomer on 4/3/20.
//
///***SeNote
//
//

import androidx.lifecycle.LiveData;

import com.aso.senote.data.models.Note;
import com.aso.senote.data.models.Notebook;
import com.aso.senote.data.source.local.LocalNotesDataSource;
import com.aso.senote.data.source.remote.RemoteNotesDataSource;

import java.util.List;

public class DefaultNotesRepository implements NotesRepository {

    private static DefaultNotesRepository INSTANCE;
    private final LocalNotesDataSource localNotesDataSource;
    private final RemoteNotesDataSource remoteNotesDataSource;

    private DefaultNotesRepository(RemoteNotesDataSource remoteNotesDataSource,
                                   LocalNotesDataSource localNotesDataSource) {
        this.localNotesDataSource = localNotesDataSource;
        this.remoteNotesDataSource = remoteNotesDataSource;
    }

    public static DefaultNotesRepository getInstance(RemoteNotesDataSource remoteNotesDataSource,
                                                     LocalNotesDataSource localNotesDataSource) {
        if (INSTANCE == null)
            INSTANCE = new DefaultNotesRepository(remoteNotesDataSource, localNotesDataSource);
        return INSTANCE;
    }

    @Override
    public LiveData<List<Notebook>> observeNotebooks() {
        return localNotesDataSource.observeNotebooks();
    }

    @Override
    public LiveData<List<Note>> observeNotes() {
        return localNotesDataSource.observeNotes();
    }

    @Override
    public LiveData<List<Note>> observeNotesForNotebook(long notebookId) {
        return localNotesDataSource.observeNotesForNotebook(notebookId);
    }

    @Override
    public LiveData<Note> observeNoteById(long noteId) {
        return localNotesDataSource.observeNoteById(noteId);
    }

    @Override
    public void saveNote(Note note) {
        localNotesDataSource.saveNote(note);
    }

    @Override
    public void saveNotebook(Notebook notebook) {
        localNotesDataSource.saveNotebook(notebook);
    }

    @Override
    public void deleteNotes(Note... notes) {
        localNotesDataSource.deleteNotes(notes);
    }

    @Override
    public void deleteNotebooks(Notebook... notebooks) {
        localNotesDataSource.deleteNotebooks(notebooks);
    }

    @Override
    public void deleteAllNotes() {
        localNotesDataSource.deleteAllNotes();
    }

    @Override
    public void deleteAllNotebooks() {
        localNotesDataSource.deleteAllNotebooks();
    }
}
