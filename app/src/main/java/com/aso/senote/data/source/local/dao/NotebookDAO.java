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

import com.aso.senote.data.models.Notebook;
import com.aso.senote.data.models.NotebookWithNotes;

import java.util.List;

@Dao
public abstract class NotebookDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract long insertNotebook(Notebook notebook);

    @Update
    abstract void updateNotebook(Notebook notebook);

    @Delete
    public abstract void deleteNotebooks(Notebook... notebooks);

    @Transaction
    public long upsert(Notebook notebook) {
        long id = insertNotebook(notebook);
        if (id == -1)
            updateNotebook(notebook);
        return id;
    }

    @Query("SELECT * from notebook_table ORDER BY date_created DESC")
    public abstract LiveData<List<Notebook>> getAllNotebooksByDateCreated();

    @Transaction
    @Query("SELECT * FROM notebook_table ORDER BY date_created DESC")
    public abstract LiveData<List<NotebookWithNotes>> getAllNotebooksWithNotes();

    @Query("DELETE FROM notebook_table")
    public abstract void deleteAllNotebooks();

}
