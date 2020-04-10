/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.ui.fragments.addEditNote

import androidx.databinding.BindingAdapter
import com.aso.senote.data.models.Note
import com.google.android.material.textfield.TextInputEditText

/** Created by ahmedsomer on 4/8/20.
 *
 ****SeNote
 *
 */

/**
 * [BindingAdapter]'s for the [Note]'s Editor.
 */
object AddEditNoteBindingAdapters {
    @JvmStatic
    @BindingAdapter("editMode")
    fun setEditable(editText: TextInputEditText, enabled: Boolean) {
        if (enabled) {
            editText.isClickable = true;
            editText.isLongClickable = true;
            editText.isFocusableInTouchMode = true;
            editText.isFocusable = true;
            editText.requestFocus();
        } else {
            editText.isClickable = false;
            editText.isLongClickable = false;
            editText.isFocusableInTouchMode = false;
            editText.isFocusable = false;
        }
    }


}