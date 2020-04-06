/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.data.database.offline;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.aso.senote.data.models.Note;
import com.aso.senote.data.models.Notebook;

@Database(entities = {Note.class, Notebook.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class NoteDatabase extends RoomDatabase {
    private static volatile NoteDatabase INSTANCE;
    private static RoomDatabase.Callback whenDatabaseCreated = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

    public static NoteDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (NoteDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NoteDatabase.class, "Notebooks.db")
                            .addCallback(whenDatabaseCreated)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract NoteDAO noteDAO();

    public abstract NotebookDAO notebookDAO();

}
