/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.data.source;

import androidx.lifecycle.LiveData;

import com.aso.senote.data.models.Note;
import com.aso.senote.data.models.Notebook;

import java.util.List;

public interface NotesRepository {

    LiveData<List<Notebook>> observeNotebooks();

    LiveData<List<Note>> observeNotes();

    LiveData<List<Note>> observeNotesForNotebook(long notebookId);

    LiveData<Note> observeNoteById(long noteId);

    void saveNote(Note note);

    void saveNotebook(Notebook notebook);

    void deleteNotes(Note... notes);

    void deleteNotebooks(Notebook... notebooks);

    void deleteAllNotes();

    void deleteAllNotebooks();

}
