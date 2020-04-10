/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.data.source

import androidx.lifecycle.LiveData
import com.aso.senote.data.models.Note
import com.aso.senote.data.models.Notebook
import com.aso.senote.data.source.local.LocalNotesDataSource
import com.aso.senote.data.source.remote.RemoteNotesDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Created by ahmedsomer on 4/3/20.
//
///***SeNote
//
//
class DefaultNotesRepository private constructor(private val remoteNotesDataSource: RemoteNotesDataSource,
                                                 private val localNotesDataSource: LocalNotesDataSource) : NotesRepository {
    override fun observeNotebooks(): LiveData<List<Notebook>?> {
        return localNotesDataSource.observeNotebooks()
    }

    override fun observeNotes(): LiveData<List<Note>?> {
        return localNotesDataSource.observeNotes()
    }

    override fun observeNotesForNotebook(notebookId: Long): LiveData<List<Note>?> {
        return localNotesDataSource.observeNotesForNotebook(notebookId)
    }

    override fun observeNoteById(noteId: Long): LiveData<Note?> {
        return localNotesDataSource.observeNoteById(noteId)
    }

    override suspend fun getNote(noteId: Long): Note {
        return withContext(Dispatchers.IO) {
            localNotesDataSource.getNote(noteId)!!
        }
    }

    override suspend fun saveNote(note: Note): Long {
        return withContext(Dispatchers.IO) {
            localNotesDataSource.saveNote(note)
        }
    }

    override suspend fun saveNotebook(notebook: Notebook) {
        withContext(Dispatchers.IO) {
            localNotesDataSource.saveNotebook(notebook)
        }
    }

    override fun deleteNotes(vararg notes: Note) {
        localNotesDataSource.deleteNotes(*notes)
    }

    override fun deleteNotebooks(vararg notebooks: Notebook) {
        localNotesDataSource.deleteNotebooks(*notebooks)
    }

    override fun deleteAllNotes() {
        localNotesDataSource.deleteAllNotes()
    }

    override fun deleteAllNotebooks() {
        localNotesDataSource.deleteAllNotebooks()
    }

    companion object {
        private var INSTANCE: DefaultNotesRepository? = null

        @JvmStatic
        fun getInstance(remoteNotesDataSource: RemoteNotesDataSource,
                        localNotesDataSource: LocalNotesDataSource): DefaultNotesRepository? {
            if (INSTANCE == null) INSTANCE = DefaultNotesRepository(remoteNotesDataSource, localNotesDataSource)
            return INSTANCE
        }
    }

}