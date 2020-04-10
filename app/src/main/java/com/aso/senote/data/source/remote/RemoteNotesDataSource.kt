/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aso.senote.data.models.Note
import com.aso.senote.data.models.Notebook
import com.aso.senote.data.source.NotesDataSource

// Created by ahmedsomer on 4/3/20.
//
///***SeNote
//
//
class RemoteNotesDataSource : NotesDataSource {
    override fun observeNotebooks(): LiveData<List<Notebook>?> {
        return MutableLiveData<List<Notebook>?>(null)
    }

    override fun observeNotes(): LiveData<List<Note>?> {
        return MutableLiveData<List<Note>?>(null)
    }

    override fun observeNotesForNotebook(notebookId: Long): LiveData<List<Note>?> {
        return MutableLiveData<List<Note>?>(null)
    }

    override fun observeNoteById(noteId: Long): LiveData<Note?> {
        return MutableLiveData<Note?>(null)

    }

    override suspend fun saveNote(note: Note): Long {
        return -1L
    }

    override suspend fun saveNotebook(notebook: Notebook) {

    }

    override fun deleteNotes(vararg notes: Note) {}
    override fun deleteNotebooks(vararg notebooks: Notebook) {}
    override fun deleteAllNotes() {}
    override fun deleteAllNotebooks() {}
    override suspend fun getNote(noteId: Long): Note? {
        return null
    }
}