/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.ui.fragments.homescreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aso.senote.Event
import com.aso.senote.data.models.Notebook
import com.aso.senote.data.source.DefaultNotesRepository

class HomeScreenViewModel(repository: DefaultNotesRepository) : ViewModel() {

    val notebooks: LiveData<List<Notebook>> = repository.observeNotebooks()

    private val _openNotebookEvent = MutableLiveData<Event<Long>>()
    val openNotebookEvent = _openNotebookEvent

    fun openNotebook(notebookId: Long) {
        _openNotebookEvent.value = Event(notebookId)
    }
}