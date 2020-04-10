/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.ui.fragments.notebook

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aso.senote.data.models.Note
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

// Created by ahmedsomer on 4/3/20.
//
///***SeNote
//
//
/**
 * [BindingAdapter]'s for the [Note]'s list.
 */
object NotebookBindingAdapters {
    @JvmStatic
    @BindingAdapter("items")
    fun setItems(recyclerView: RecyclerView, items: List<Note?>?) {
        (Objects.requireNonNull(recyclerView.adapter) as NotebookAdapter).submitList(items)
    }

    @JvmStatic
    @BindingAdapter("showLastModified")
    fun showLastModified(textView: TextView, calendar: Calendar) {
        // TODO: 3/29/20 unfinished
        val date = calendar.time
        val formatter: DateFormat = SimpleDateFormat("EEEE,hh:mm:ss a", Locale.getDefault())
        textView.text = formatter.format(date)
    }
}