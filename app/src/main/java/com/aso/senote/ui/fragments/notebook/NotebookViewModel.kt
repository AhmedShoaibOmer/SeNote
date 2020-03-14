/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.ui.fragments.notebook

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aso.senote.data.models.Note
import com.aso.senote.data.source.DefaultNotesRepository

/** Created by ahmedsomer on 3/19/20.
 *
 ****SeNote
 *
 */
class NotebookViewModel(repository: DefaultNotesRepository) : ViewModel() {

    val notes: LiveData<MutableList<Note>> = repository.observeNotes()

    fun openNote(noteId: Long) {}

}