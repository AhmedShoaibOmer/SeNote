/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.ui.fragments.homeScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aso.senote.Event

class HomeScreenViewModel : ViewModel() {

    private val _insert = MutableLiveData<Event<Type>>()
    val insert: LiveData<Event<Type>> = _insert

    fun insert(type: Int) {

        val type1: Type = when (type) {
            2 -> Type.TEXT
            3 -> Type.TODO_LIST
            6 -> Type.HAND_WRITING
            1 -> Type.ATTACHMENT
            4 -> Type.PICTURE
            5 -> Type.VOICE_NOTE
            else -> return
        }
        _insert.value = Event(type1)
    }

    private val _showInsertMenu = MutableLiveData<Event<Boolean>>()
    val showInsertMenu: LiveData<Event<Boolean>> = _showInsertMenu

    fun showInsertMenu(show: Boolean) {
        _showInsertMenu.value = Event(show)
    }

    companion object {
        enum class Type {
            TEXT,
            VOICE_NOTE,
            TODO_LIST,
            ATTACHMENT,
            HAND_WRITING,
            PICTURE
        }
    }

}
