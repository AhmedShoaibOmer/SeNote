/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table", indices = @Index(value = "noteId"))
public class Note {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "noteId")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
