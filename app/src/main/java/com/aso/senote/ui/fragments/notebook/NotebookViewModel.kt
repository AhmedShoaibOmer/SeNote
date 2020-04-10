/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.ui.fragments.notebook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.aso.senote.Event
import com.aso.senote.data.models.Note
import com.aso.senote.data.source.DefaultNotesRepository

/** Created by ahmedsomer on 3/19/20.
 *
 ****SeNote
 *
 */
class NotebookViewModel(repository: DefaultNotesRepository) : ViewModel() {

    private val _dataLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _notebookId: MutableLiveData<Long> = MutableLiveData()

    private val _notes: LiveData<List<Note>?> = _notebookId.switchMap {
        repository.observeNotesForNotebook(it)
    }

    val notes: LiveData<List<Note>?> = _notes

    private val _openNoteEvent = MutableLiveData<Event<Long>>()
    val openNoteEvent = _openNoteEvent

    fun openNote(noteId: Long) {
        _openNoteEvent.value = Event(noteId)
    }

    fun loadNotes(notebookId: Long) {
        //If we're already loading or already loaded, return (might be a config change)
        if (_dataLoading.value == true || notebookId == _notebookId.value) {
            return
        }

        //Trigger the load
        _notebookId.value = notebookId
    }

}