/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aso.senote.R

/**
 * A simple [Fragment] subclass.
 */
class NoteBookFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_book, container, false)
    }

}
