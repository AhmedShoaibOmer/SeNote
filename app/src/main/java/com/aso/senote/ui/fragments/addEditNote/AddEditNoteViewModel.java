/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.ui.fragments.addEditNote;

import androidx.lifecycle.ViewModel;

import com.aso.senote.data.source.DefaultNotesRepository;

// Created by ahmedsomer on 4/1/20.
//
///***SeNote
//
//

public class AddEditNoteViewModel extends ViewModel {

    private final DefaultNotesRepository repository;

    public AddEditNoteViewModel(DefaultNotesRepository repository) {
        this.repository = repository;
    }
}
