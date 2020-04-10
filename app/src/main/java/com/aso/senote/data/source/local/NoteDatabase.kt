/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aso.senote.AppExecutors
import com.aso.senote.R
import com.aso.senote.data.models.Note
import com.aso.senote.data.models.Notebook
import com.aso.senote.data.source.local.converter.DateConverter
import com.aso.senote.data.source.local.dao.NoteDAO
import com.aso.senote.data.source.local.dao.NotebookDAO

@Database(entities = [Note::class, Notebook::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDAO(): NoteDAO
    abstract fun notebookDAO(): NotebookDAO

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        @JvmStatic
        fun getInstance(context: Context, executors: AppExecutors): NoteDatabase? {
            if (INSTANCE == null) {
                synchronized(NoteDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                NoteDatabase::class.java, "Notebooks.db")
                                .addCallback(object : Callback() {
                                    override fun onCreate(db: SupportSQLiteDatabase) {
                                        super.onCreate(db)
                                        executors.localIO().execute {
                                            val defaultNotebookId = INSTANCE!!.notebookDAO().upsert(Notebook("My Notebook",
                                                    null))
                                            storeTheDefaultNotebookId(context, defaultNotebookId)
                                            insertFirstNote(defaultNotebookId)
                                        }
                                    }

                                    override fun onOpen(db: SupportSQLiteDatabase) {
                                        super.onOpen(db)
                                    }
                                }
                                )
                                .build()
                    }
                }
            }
            return INSTANCE
        }

        private fun insertFirstNote(defaultNotebookId: Long) {
            val note = Note(defaultNotebookId, "First Note",
                    "blah blah blah blah blah " +
                            "blah blah blah blah blah blah blah blah blah blah blah blah blah blah " +
                            " blah blah blah blah blah")
            note.isStarred = true
            INSTANCE!!.noteDAO().upsert(note)
            val note1 = Note(defaultNotebookId, "First Note",
                    "blah blah blah blah blah " +
                            "blah blah blah blah blah blah blah blah blah blah blah blah blah blah " +
                            "blah blah blah blah blah blah blah" +
                            " blah blah blah blah blah")
            note1.isStarred = true
            INSTANCE!!.noteDAO().upsert(note1)
            INSTANCE!!.noteDAO().upsert(Note(defaultNotebookId, "First Note",
                    "blah blah blah blah blah "))
            INSTANCE!!.noteDAO().upsert(Note(defaultNotebookId, "First Note",
                    "blah blah blah blah blah " +
                            "blah blah blah blah blah blah blah blah blah blah blah blah blah blah " +
                            "blah blah blah blah blah blah blah" +
                            " blah blah blah blah blah" +
                            " blah blah blah blah blah " +
                            "blah blah blah blah blah blah blah blah blah blah blah blah blah blah " +
                            "blah blah blah blah blah blah blah" +
                            " blah blah blah blah blah"))
            INSTANCE!!.noteDAO().upsert(Note(defaultNotebookId, "First Note",
                    "blah blah blah blah blah " +
                            "blah blah blah blah blah blah blah blah blah blah blah blah blah blah " +
                            "blah blah blah blah blah blah blah" +
                            " blah blah blah blah blah"))
            INSTANCE!!.noteDAO().upsert(Note(defaultNotebookId, "First Note",
                    "blah blah blah blah blah " +
                            "blah blah blah blah blah blah blah blah blah blah blah blah blah blah " +
                            "blah blah blah blah blah blah blah" +
                            " blah blah blah blah blah"))
        }

        private fun storeTheDefaultNotebookId(context: Context, defaultNotebookId: Long) {
            val preferences = context.getSharedPreferences(
                    context.resources.getString(R.string.PREFERENCES_FILE_KEY),
                    Context.MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putLong(context.resources.getString(R.string.DEFAULT_NOTEBOOK_ID), defaultNotebookId)
            editor.apply()
        }
    }
}