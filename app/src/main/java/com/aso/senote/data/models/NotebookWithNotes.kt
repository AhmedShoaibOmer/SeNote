/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.data.models

import androidx.room.Embedded
import androidx.room.Relation

class NotebookWithNotes(@field:Embedded var notebook: Notebook,
                        @field:Relation(parentColumn = "notebookId",
                                entityColumn = "notebookId")
                        var notes: List<Note>) {

    override fun toString(): String {
        return "NotebookWithNotes{" +
                "notebook=" + notebook +
                ", notes=" + notes +
                '}'
    }

}