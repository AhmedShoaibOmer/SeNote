/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notebook_table",
        indices = [Index(value = ["notebookId"])])
class Notebook(var notebookTitle: String, var notebookCoverUri: String) {
    @ColumnInfo(name = "date_created")
    var dateCreated: Calendar

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "notebookId")
    var notebookId: Long = 0
    var lastModified: Calendar

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is Notebook) return false
        return notebookId == o.notebookId &&
                dateCreated == o.dateCreated &&
                notebookTitle == o.notebookTitle &&
                notebookCoverUri == o.notebookCoverUri &&
                lastModified == o.lastModified
    }

    override fun hashCode(): Int {
        return Objects.hash(dateCreated, notebookId, notebookTitle, notebookCoverUri, lastModified)
    }

    init {
        lastModified = Calendar.getInstance()
        // TODO: 3/14/20 Deal with the Time zone Differing
        dateCreated = Calendar.getInstance()
    }
}
