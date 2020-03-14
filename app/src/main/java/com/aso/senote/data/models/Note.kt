/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.data.models

import androidx.room.*
import java.util.*

@Entity(tableName = "note_table",
        indices = [Index(value = ["noteId"]),
            Index(value = ["notebookId"])],
        foreignKeys = [
            ForeignKey(entity = Notebook::class,
                    parentColumns = ["notebookId"],
                    childColumns = ["notebookId"],
                    onDelete = ForeignKey.CASCADE)
        ])
class Note {
    @ColumnInfo(name = "date_created")
    var dateCreated: Calendar

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "noteId")
    var id: Long = 0
    var notebookId: Long = 0
    var noteTitle: String? = null
    var noteContent: String? = null
    var isStarred = false
    var lastModified: Calendar

    constructor(notebookId: Long, noteTitle: String?, noteContent: String?) {
        dateCreated = Calendar.getInstance()
        this.notebookId = notebookId
        this.noteTitle = noteTitle
        this.noteContent = noteContent
        lastModified = Calendar.getInstance()
        isStarred = false
    }

    @Ignore
    constructor() {
        dateCreated = Calendar.getInstance()
        lastModified = Calendar.getInstance()
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is Note) return false
        val note = o
        return id == note.id && notebookId == note.notebookId && isStarred == note.isStarred && dateCreated == note.dateCreated &&
                noteTitle == note.noteTitle &&
                noteContent == note.noteContent && lastModified == note.lastModified
    }

    override fun hashCode(): Int {
        return Objects.hash(dateCreated, id, notebookId, noteTitle, noteContent, isStarred, lastModified)
    }
}