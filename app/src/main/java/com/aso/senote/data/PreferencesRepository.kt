/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/** Created by ahmedsomer on 4/14/20.
 *
 ****SeNote
 *
 */

/**
 * A simple data repository for in-app settings.
 */
class PreferenceRepository(private val sharedPreferences: SharedPreferences) {


    var defaultNotebookId: Long = PREFERENCE_DEFAULT_NOTEBOOK_ID_DEF_VAL
        get() = sharedPreferences.getLong(PREFERENCE_DEFAULT_NOTEBOOK_ID,
                PREFERENCE_DEFAULT_NOTEBOOK_ID_DEF_VAL)
        set(value) {
            sharedPreferences.edit().putLong(PREFERENCE_DEFAULT_NOTEBOOK_ID, value).apply()
            field = value
        }

    private val _defaultNotebookIdLive: MutableLiveData<Long> = MutableLiveData()
    val defaultNotebookIdLive: LiveData<Long>
        get() = _defaultNotebookIdLive

    private val nightMode: Int
        get() = sharedPreferences.getInt(PREFERENCE_NIGHT_MODE, PREFERENCE_NIGHT_MODE_DEF_VAL)

    private val _nightModeLive: MutableLiveData<Int> = MutableLiveData()
    val nightModeLive: LiveData<Int>
        get() = _nightModeLive

    var isDarkTheme: Boolean = false
        get() = nightMode == AppCompatDelegate.MODE_NIGHT_YES
        set(value) {
            sharedPreferences.edit().putInt(PREFERENCE_NIGHT_MODE, if (value) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }).apply()
            field = value
        }

    private val _isDarkThemeLive: MutableLiveData<Boolean> = MutableLiveData()
    val isDarkThemeLive: LiveData<Boolean>
        get() = _isDarkThemeLive

    private val preferenceChangedListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                when (key) {
                    PREFERENCE_NIGHT_MODE -> {
                        _nightModeLive.value = nightMode
                        _isDarkThemeLive.value = isDarkTheme
                    }
                    PREFERENCE_DEFAULT_NOTEBOOK_ID -> {
                        _defaultNotebookIdLive.value = defaultNotebookId
                    }
                }
            }

    init {
        // Init preference LiveData objects.
        _nightModeLive.value = nightMode
        _isDarkThemeLive.value = isDarkTheme
        _defaultNotebookIdLive.value = defaultNotebookId

        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangedListener)
    }

    companion object {
        private const val PREFERENCE_NIGHT_MODE = "preference_night_mode"
        private const val PREFERENCE_NIGHT_MODE_DEF_VAL = AppCompatDelegate.MODE_NIGHT_NO
        private const val PREFERENCE_DEFAULT_NOTEBOOK_ID = "Default NoteBook ID"
        private const val PREFERENCE_DEFAULT_NOTEBOOK_ID_DEF_VAL = -1L

    }
}
