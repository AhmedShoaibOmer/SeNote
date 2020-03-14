/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aso.senote.data.source.DefaultNotesRepository
import com.aso.senote.ui.fragments.addEditNote.AddEditNoteViewModel
import com.aso.senote.ui.fragments.homescreen.HomeScreenViewModel
import com.aso.senote.ui.fragments.notebook.NotebookViewModel

// Created by ahmedsomer on 4/3/20.
//
///***SeNote
//
//
/**
 * Factory for all [ViewModel]'s.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: DefaultNotesRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = (
            when {
                modelClass.isAssignableFrom(HomeScreenViewModel::class.java) ->
                    HomeScreenViewModel(repository)
                modelClass.isAssignableFrom(NotebookViewModel::class.java) ->
                    NotebookViewModel(repository)
                modelClass.isAssignableFrom(AddEditNoteViewModel::class.java) ->
                    AddEditNoteViewModel(repository)
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }) as T

}