/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.data.database.offline;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.aso.senote.data.models.Note;

import java.util.List;

@Dao
public interface NoteDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNotes(Note... note);

    @Query("SELECT * from note_table ORDER BY date_created DESC")
    LiveData<List<Note>> getAllNotesByDateCreated();

    @Query("DELETE FROM note_table")
    void deleteAllNotes();
}
