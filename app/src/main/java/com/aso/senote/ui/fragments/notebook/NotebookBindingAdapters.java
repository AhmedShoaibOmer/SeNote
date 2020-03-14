/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.ui.fragments.notebook;
// Created by ahmedsomer on 4/3/20.
//
///***SeNote
//
//

import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.aso.senote.data.models.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * [BindingAdapter]'s for the [Note]'s list.
 */
public class NotebookBindingAdapters {

    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView, List<Note> items) {
        ((NotesAdapter) Objects.requireNonNull(recyclerView.getAdapter())).submitList(items);
    }

    @BindingAdapter({"showLastModified"})
    public static void showLastModified(TextView textView, Calendar calendar) {
        // TODO: 3/29/20 unfinished
        Date date = calendar.getTime();
        DateFormat formatter = new SimpleDateFormat("EEEE,hh:mm:ss a", Locale.getDefault());
        textView.setText(formatter.format(date));
    }
}
