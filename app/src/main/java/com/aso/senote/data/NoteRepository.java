/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.aso.senote.data.database.offline.NoteDAO;
import com.aso.senote.data.database.offline.NoteDatabase;
import com.aso.senote.data.database.offline.NotebookDAO;
import com.aso.senote.data.models.Note;
import com.aso.senote.data.models.Notebook;
import com.aso.senote.data.models.NotebookWithNotes;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {
    private static final int NUMBER_OF_THREADS = 4;
    private static ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private final LiveData<List<NotebookWithNotes>> mAllNotebooksWithNotes;
    private final LiveData<List<Notebook>> mAllNotebooks;
    private final LiveData<List<Note>> mAllNotes;
    private NotebookDAO mNotebookDao;
    private NoteDAO mNoteDao;
    private NoteRepository instance;

    private NoteRepository(Application application) {
        NoteDatabase db = NoteDatabase.getInstance(application);
        mNoteDao = db.noteDAO();
        mNotebookDao = db.notebookDAO();
        mAllNotebooksWithNotes = mNotebookDao.getAllNotebooksWithNotes();
        mAllNotebooks = mNotebookDao.getAllNotebooksByDateCreated();
        mAllNotes = mNoteDao.getAllNotesByDateCreated();
    }

    public NoteRepository getInstance(Application application) {
        if (instance == null) instance = new NoteRepository(application);
        return instance;
    }

    public void insertNote(Note note) {
        databaseWriteExecutor.execute(() -> mNoteDao.insertNote(note));
    }

    public void insertNotebook(Notebook notebook) {
        databaseWriteExecutor.execute(() -> mNotebookDao.insertNotebook(notebook));
    }

    public void deleteNotes(Note... notes) {
        databaseWriteExecutor.execute(() -> mNoteDao.deleteNotes(notes));
    }

    public void deleteNotebooks(Notebook... notebooks) {
        databaseWriteExecutor.execute(() -> mNotebookDao.deleteNotebooks(notebooks));
    }

    public void updateNote(Note note) {
        databaseWriteExecutor.execute(() -> mNoteDao.updateNote(note));
    }

    public void updateNotebook(Notebook notebook) {
        databaseWriteExecutor.execute(() -> mNotebookDao.updateNotebook(notebook));
    }

    public void deleteAllNotes() {
        databaseWriteExecutor.execute(() -> mNoteDao.deleteAllNotes());
    }

    public void deleteAllNotebooks() {
        databaseWriteExecutor.execute(() -> mNotebookDao.deleteAllNotebooks());
    }

    public LiveData<List<NotebookWithNotes>> getAllNotebooksWithNotes() {
        return mAllNotebooksWithNotes;
    }

    public LiveData<List<Notebook>> getAllNotebooks() {
        return mAllNotebooks;
    }

    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

}
