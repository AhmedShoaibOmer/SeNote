/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aso.senote.data.models.Note

@Dao
abstract class NoteDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertNote(note: Note): Long

    @Update
    abstract fun updateNote(note: Note)

    @Delete
    abstract fun deleteNotes(vararg note: Note)

    @Transaction
    open fun upsert(note: Note): Long {
        val id = insertNote(note)
        if (id == -1L) updateNote(note)
        return id
    }

    @Query("SELECT * from note_table WHERE notebookId = :NotebookId ORDER BY date_created DESC")
    abstract fun getNotesForNotebook(NotebookId: Long): LiveData<List<Note>?>

    @Query("SELECT * from note_table WHERE noteId = :noteId")
    abstract fun getNoteById(noteId: Long): LiveData<Note?>

    @Query("SELECT * from note_table WHERE noteId = :noteId")
    abstract fun getNote(noteId: Long): Note?

    @get:Query("SELECT * from note_table ORDER BY date_created DESC")
    abstract val allNotesByDateCreated: LiveData<List<Note>?>

    @Query("DELETE FROM note_table")
    abstract fun deleteAllNotes()
}