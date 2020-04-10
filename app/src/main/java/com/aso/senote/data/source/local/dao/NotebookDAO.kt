/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aso.senote.data.models.Notebook
import com.aso.senote.data.models.NotebookWithNotes

@Dao
abstract class NotebookDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertNotebook(notebook: Notebook?): Long

    @Update
    abstract fun updateNotebook(notebook: Notebook)

    @Delete
    abstract fun deleteNotebooks(vararg notebooks: Notebook)

    @Transaction
    open fun upsert(notebook: Notebook): Long {
        val id = insertNotebook(notebook)
        if (id == -1L) updateNotebook(notebook)
        return id
    }

    @get:Query("SELECT * from notebook_table ORDER BY date_created DESC")
    abstract val allNotebooksByDateCreated: LiveData<List<Notebook>?>

    @get:Query("SELECT * FROM notebook_table ORDER BY date_created DESC")
    @get:Transaction
    abstract val allNotebooksWithNotes: LiveData<List<NotebookWithNotes>>

    @Query("DELETE FROM notebook_table")
    abstract fun deleteAllNotebooks()

    @Query("SELECT * from notebook_table WHERE notebookId = :notebookId")
    abstract fun observeNotebookById(notebookId: Long): LiveData<Notebook>
}