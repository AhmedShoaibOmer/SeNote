/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.ui.fragments.addEditNote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aso.senote.Event
import com.aso.senote.R
import com.aso.senote.data.models.Note
import com.aso.senote.data.source.DefaultNotesRepository
import com.aso.senote.ui.activities.MainActivity
import kotlinx.coroutines.launch

// Created by ahmedsomer on 4/1/20.
//
///***SeNote
//
//
class AddEditNoteViewModel(private val repository: DefaultNotesRepository) : ViewModel() {


    val isEditMode = MutableLiveData(false)

    // Two-way DataBinding, exposing MutableLiveData
    val title = MutableLiveData<String>()

    // Two-way DataBinding, exposing MutableLiveData
    val content = MutableLiveData<String>()

    // Two-way DataBinding, exposing MutableLiveData
    val lineSpacing = MutableLiveData<Int>()

    // Two-way DataBinding, exposing MutableLiveData
    val leftMargin = MutableLiveData<Int>()

    // Two-way DataBinding, exposing MutableLiveData
    val rightMargin = MutableLiveData<Int>()

    // Two-way DataBinding, exposing MutableLiveData
    val textSize = MutableLiveData<Int>()

    private val _isStarred = MutableLiveData(false)
    val isStarred: LiveData<Boolean> = _isStarred

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _toastText = MutableLiveData<Event<Int>>()
    val toastText: LiveData<Event<Int>> = _toastText

    private val _noteUpdatedEvent = MutableLiveData<Event<Unit>>()
    val noteUpdatedEvent: LiveData<Event<Unit>> = _noteUpdatedEvent

    private var currentNote = Note()

    private var noteId: Long = -1

    private var notebookId: Long = -1

    private var isNewNote = false

    private var isDataLoaded = false

    fun start(noteId: Long, notebookId: Long, action: MainActivity.Action) {
        initPreferences()
        if (_dataLoading.value == true) {
            return
        }

        this.noteId = noteId
        this.notebookId = notebookId

        if (action == MainActivity.Action.ADD) {
            // No need to populate it's a new Note'
            isNewNote = true
            isEditMode.value = true
            return
        }

        if (isDataLoaded) {
            //No need to populate, already have data.
            return
        }

        isNewNote = false
        _dataLoading.value = true
        viewModelScope.launch {
            repository.getNote(noteId).let {
                if (it == null) {
                    onDataNotAvailable()
                } else {
                    onDataLoaded(it)
                }
            }
        }
    }

    private fun onDataNotAvailable() {
        _dataLoading.value = false
    }

    private fun onDataLoaded(note: Note) {
        currentNote = note
        title.value = note.noteTitle
        content.value = note.noteContent
        _isStarred.value = note.isStarred

        _dataLoading.value = false
        isDataLoaded = true
        isEditMode.value = false
    }

    fun saveNote() {

        Log.w(TAG, "save fun invoked")
        val currentTitle = title.value
        val currentContent = content.value

        if (currentTitle != null && currentTitle.isNotEmpty()) {
            if (currentContent != null && currentContent.isNotEmpty()) {
                createOrUpdateNote(currentTitle, currentContent)
            } else {
                createOrUpdateNote(currentTitle, currentTitle)
                content.value = currentTitle
            }

        } else {
            if (currentContent != null && currentContent.isNotEmpty()) {
                createOrUpdateNote(currentTitle, currentContent)
            } else {
                emptyNote()
            }
        }
        isEditMode.value = false
    }

    fun setStarred(isStarred: Boolean) {
        _isStarred.value = isStarred
        currentNote.isStarred = isStarred
        viewModelScope.launch {
            repository.saveNote(currentNote)
        }
    }

    private fun emptyNote() {
        _toastText.value = Event(R.string.empty_note_message)
    }

    private fun createOrUpdateNote(currentTitle: String?, currentContent: String) {
        currentNote.noteTitle = currentTitle
        currentNote.noteContent = currentContent
        viewModelScope.launch {
            if (isNewNote) {
                currentNote.notebookId = notebookId
                noteId = repository.saveNote(currentNote)
                currentNote.id = noteId
                isNewNote = !isNewNote
            } else {
                repository.saveNote(currentNote)
            }
        }
    }


    private fun initPreferences() {

        lineSpacing.value = 6
        leftMargin.value = 14
        rightMargin.value = 12
        textSize.value = 14
    }

    fun cancel() {
        isEditMode.value = false
        if (isNoteEmpty()) {
            return
        }
        title.value = currentNote.noteTitle
        content.value = currentNote.noteContent
    }

    fun isNoteEmpty(): Boolean {
        if (currentNote.noteTitle == null || currentNote.noteTitle!!.isEmpty()) {
            if (currentNote.noteContent == null || currentNote.noteContent!!.isEmpty()) {
                return true
            }
        }
        return false
    }

    companion object {
        private const val TAG = "AddEditNoteViewModel"
    }
}