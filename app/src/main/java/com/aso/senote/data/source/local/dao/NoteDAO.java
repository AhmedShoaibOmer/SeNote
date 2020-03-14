/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.data.source.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.aso.senote.data.models.Note;

import java.util.List;

@Dao
public abstract class NoteDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract long insertNote(Note note);

    @Update
    abstract void updateNote(Note note);

    @Delete
    public abstract void deleteNotes(Note... note);

    @Transaction
    public void upsert(Note note) {
        long id = insertNote(note);
        if (id == -1)
            updateNote(note);
    }

    @Query("SELECT * from note_table WHERE notebookId = :NotebookId ORDER BY date_created DESC")
    public abstract LiveData<List<Note>> getNotesForNotebook(long NotebookId);

    @Query("SELECT * from note_table WHERE noteId = :noteId")
    public abstract LiveData<Note> getNoteById(long noteId);

    @Query("SELECT * from note_table ORDER BY date_created DESC")
    public abstract LiveData<List<Note>> getAllNotesByDateCreated();

    @Query("DELETE FROM note_table")
    public abstract void deleteAllNotes();
}
