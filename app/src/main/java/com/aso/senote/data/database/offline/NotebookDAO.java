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
import androidx.room.Transaction;
import androidx.room.Update;

import com.aso.senote.data.models.Notebook;
import com.aso.senote.data.models.NotebookWithNotes;

import java.util.List;

@Dao
public interface NotebookDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNotebook(Notebook notebook);

    @Update
    void updateNotebook(Notebook notebook);

    @Delete
    void deleteNotebooks(Notebook... notebooks);

    @Query("SELECT * from notebook_table ORDER BY date_created DESC")
    LiveData<List<Notebook>> getAllNotebooksByDateCreated();

    @Transaction
    @Query("SELECT * FROM notebook_table")
    LiveData<List<NotebookWithNotes>> getAllNotebooksWithNotes();

    @Query("DELETE FROM notebook_table")
    void deleteAllNotebooks();

}
