/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.aso.senote.AppExecutors;
import com.aso.senote.R;
import com.aso.senote.data.models.Note;
import com.aso.senote.data.models.Notebook;
import com.aso.senote.data.source.local.converter.DateConverter;
import com.aso.senote.data.source.local.dao.NoteDAO;
import com.aso.senote.data.source.local.dao.NotebookDAO;

@Database(entities = {Note.class, Notebook.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class NoteDatabase extends RoomDatabase {
    private static volatile NoteDatabase INSTANCE;

    public static NoteDatabase getInstance(Context context, AppExecutors executors) {
        if (INSTANCE == null) {
            synchronized (NoteDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NoteDatabase.class, "Notebooks.db")
                            .addCallback(new RoomDatabase.Callback() {

                                             @Override
                                             public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                                 super.onCreate(db);
                                                 executors.localIO().execute(() -> {

                                                     long defaultNotebookId = INSTANCE.notebookDAO().upsert(new Notebook("My Notebook",
                                                             null));

                                                     storeTheDeaultNotebookId(context, defaultNotebookId);

                                                     insertFirstNote(defaultNotebookId);
                                                 });
                                             }

                                             @Override
                                             public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                                 super.onOpen(db);
                                             }
                                         }
                            )
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static void insertFirstNote(long defaultNotebookId) {
        INSTANCE.noteDAO().upsert(new Note(defaultNotebookId, "First Note",
                "blah blah blah blah blah " +
                        "blah blah blah blah blah blah blah blah blah blah blah blah blah blah " +
                        " blah blah blah blah blah"));
        INSTANCE.noteDAO().upsert(new Note(defaultNotebookId, "First Note",
                "blah blah blah blah blah " +
                        "blah blah blah blah blah blah blah blah blah blah blah blah blah blah " +
                        "blah blah blah blah blah blah blah" +
                        " blah blah blah blah blah"));
        INSTANCE.noteDAO().upsert(new Note(defaultNotebookId, "First Note",
                "blah blah blah blah blah "));
        INSTANCE.noteDAO().upsert(new Note(defaultNotebookId, "First Note",
                "blah blah blah blah blah " +
                        "blah blah blah blah blah blah blah blah blah blah blah blah blah blah " +
                        "blah blah blah blah blah blah blah" +
                        " blah blah blah blah blah" +
                        " blah blah blah blah blah " +
                        "blah blah blah blah blah blah blah blah blah blah blah blah blah blah " +
                        "blah blah blah blah blah blah blah" +
                        " blah blah blah blah blah"));
        INSTANCE.noteDAO().upsert(new Note(defaultNotebookId, "First Note",
                "blah blah blah blah blah " +
                        "blah blah blah blah blah blah blah blah blah blah blah blah blah blah " +
                        "blah blah blah blah blah blah blah" +
                        " blah blah blah blah blah"));
        INSTANCE.noteDAO().upsert(new Note(defaultNotebookId, "First Note",
                "blah blah blah blah blah " +
                        "blah blah blah blah blah blah blah blah blah blah blah blah blah blah " +
                        "blah blah blah blah blah blah blah" +
                        " blah blah blah blah blah"));

    }

    private static void storeTheDeaultNotebookId(Context context, long defaultNotebookId) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getResources().getString(R.string.PREFERENCES_FILE_KEY),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(context.getResources().getString(R.string.DEFAULT_NOTEBOOK_ID), defaultNotebookId);
        editor.apply();
    }

    public abstract NoteDAO noteDAO();

    public abstract NotebookDAO notebookDAO();

}
