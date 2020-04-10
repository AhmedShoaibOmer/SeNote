/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.data.source

import androidx.lifecycle.LiveData
import com.aso.senote.data.models.Note
import com.aso.senote.data.models.Notebook

// Created by ahmedsomer on 4/3/20.
//
///***SeNote
//
//
interface NotesDataSource {
    fun observeNotebooks(): LiveData<List<Notebook>?>
    fun observeNotes(): LiveData<List<Note>?>
    fun observeNotesForNotebook(notebookId: Long): LiveData<List<Note>?>
    fun observeNoteById(noteId: Long): LiveData<Note?>
    suspend fun saveNote(note: Note): Long
    suspend fun saveNotebook(notebook: Notebook)
    fun deleteNotes(vararg notes: Note)
    fun deleteNotebooks(vararg notebooks: Notebook)
    fun deleteAllNotes()
    fun deleteAllNotebooks()
    suspend fun getNote(noteId: Long): Note?
}