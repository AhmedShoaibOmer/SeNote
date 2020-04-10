/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.ui.fragments.notebooks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aso.senote.Event
import com.aso.senote.data.models.Notebook
import com.aso.senote.data.source.DefaultNotesRepository
import kotlinx.coroutines.launch

class NotebooksViewModel(private val repository: DefaultNotesRepository?) : ViewModel() {

    val notebooks: LiveData<List<Notebook>?> = repository!!.observeNotebooks()

    private val _openNotebookEvent = MutableLiveData<Event<Long>>()
    val openNotebookEvent: LiveData<Event<Long>> = _openNotebookEvent

    fun openNotebook(notebookId: Long) {
        _openNotebookEvent.value = Event(notebookId)
    }

    fun createNotebook(notebook: Notebook) {
        viewModelScope.launch {
            repository!!.saveNotebook(notebook)
        }
    }
}