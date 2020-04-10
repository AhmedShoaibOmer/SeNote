/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.data.source.local

import android.app.Application
import androidx.lifecycle.LiveData
import com.aso.senote.AppExecutors
import com.aso.senote.BaseApplication
import com.aso.senote.data.models.Note
import com.aso.senote.data.models.Notebook
import com.aso.senote.data.source.NotesDataSource
import com.aso.senote.data.source.local.dao.NoteDAO
import com.aso.senote.data.source.local.dao.NotebookDAO

// Created by ahmedsomer on 4/2/20.
//
///***SeNote
//
//
class LocalNotesDataSource(
        application: Application,
        private val mAppExecutors: AppExecutors
) : NotesDataSource {

    private val mNotebookDao: NotebookDAO
    private val mNoteDao: NoteDAO
    private val mAllNotes: LiveData<List<Note>?>
    private val mAllNotebooks: LiveData<List<Notebook>?>

    init {
        val db = (application as BaseApplication).dataBase
        mNoteDao = db!!.noteDAO()
        mNotebookDao = db.notebookDAO()
        mAllNotebooks = mNotebookDao.allNotebooksByDateCreated
        mAllNotes = mNoteDao.allNotesByDateCreated
    }

    override fun observeNotebooks(): LiveData<List<Notebook>?> {
        return mAllNotebooks
    }

    override fun observeNotes(): LiveData<List<Note>?> {
        return mAllNotes
    }

    override fun observeNotesForNotebook(notebookId: Long): LiveData<List<Note>?> {

        return mNoteDao.getNotesForNotebook(notebookId)
    }

    override fun observeNoteById(noteId: Long): LiveData<Note?> {
        return mNoteDao.getNoteById(noteId)
    }

    override suspend fun saveNote(note: Note): Long {
        return mNoteDao.upsert(note)
    }

    override suspend fun saveNotebook(notebook: Notebook) {
        mAppExecutors.localIO().execute { mNotebookDao.upsert(notebook) }

    }

    override suspend fun getNote(noteId: Long): Note? {
        return mNoteDao.getNote(noteId)
    }

    override fun observeNotebookById(notebookId: Long): LiveData<Notebook> {
        return mNotebookDao.observeNotebookById(notebookId)
    }

    override fun deleteNotes(vararg notes: Note) {
        mAppExecutors.localIO().execute { mNoteDao.deleteNotes(*notes) }
    }

    override fun deleteNotebooks(vararg notebooks: Notebook) {
        mAppExecutors.localIO().execute { mNotebookDao.deleteNotebooks(*notebooks) }
    }

    override fun deleteAllNotes() {
        mAppExecutors.localIO().execute { mNoteDao.deleteAllNotes() }
    }

    override fun deleteAllNotebooks() {
        mAppExecutors.localIO().execute { mNotebookDao.deleteAllNotebooks() }
    }
}