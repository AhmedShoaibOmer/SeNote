/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.util

import androidx.fragment.app.Fragment
import com.aso.senote.BaseApplication
import com.aso.senote.ViewModelFactory
import com.aso.senote.data.PreferenceRepository

/** Created by ahmedsomer on 4/5/20.
 *
 ****SeNote
 *
 */

/**
 * Extension functions for Fragment.
 * */
fun Fragment.getViewModelFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as BaseApplication).defaultNotesRepository
    return ViewModelFactory(repository)
}

fun Fragment.getPreferencesRepository(): PreferenceRepository {
    return (requireContext().applicationContext as BaseApplication).preferenceRepository
}