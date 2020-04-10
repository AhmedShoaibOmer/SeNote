/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.ui.fragments.notebook

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.aso.senote.EventObserver
import com.aso.senote.R
import com.aso.senote.databinding.FragmentNotebookBinding
import com.aso.senote.ui.activities.MainActivity
import com.aso.senote.ui.fragments.addEditNote.AddEditNoteFragmentArgs
import com.aso.senote.util.getViewModelFactory
import com.google.android.material.transition.MaterialContainerTransform
import java.util.*

/**
 * Display a grid of [Note]s for a specific [Notebook] using its Id.
 */
class NotebookFragment : Fragment() {
    private val mViewModel by viewModels<NotebookViewModel> { getViewModelFactory() }

    private val args: NotebookFragmentArgs by navArgs()

    private lateinit var binding: FragmentNotebookBinding

    private lateinit var listAdapter: NotebookAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotebookBinding.inflate(
                inflater, container, false).apply {
            viewModel = mViewModel
        }

        mViewModel.loadNotes(args.notebookId)

        postponeEnterTransition()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = this.viewLifecycleOwner
        setupListAdapter()
        setupNavigation()
        mViewModel.empty.observe(viewLifecycleOwner, Observer {
            if (it) {
                val quotes = resources.getStringArray(R.array.quotes)
                val randomQuote = Random().nextInt(quotes.size)
                binding.quoteTv.text = quotes[randomQuote]
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startPostponedEnterTransition()
    }

    private fun setupNavigation() {
        mViewModel.openNoteEvent.observe(viewLifecycleOwner, EventObserver {
            openNote(it)
        })
    }

    private fun openNote(noteId: Long) {
        val args = AddEditNoteFragmentArgs.Builder(MainActivity.Action.PREVIEW)
                .setNoteId(noteId)
                .build()
                .toBundle()
        Navigation.findNavController(requireActivity(), R.id.main_fragment_container)
                .navigate(R.id.action_homeScreenFragment_to_addEditNoteFragment, args)

    }

    private fun setupListAdapter() {
        val viewModel = binding.viewModel
        if (viewModel != null) {
            listAdapter = NotebookAdapter(viewModel)
            binding.notebookRv.adapter = listAdapter
        } else {
            Log.w(TAG, "ViewModel not initialized when attempting to setup adapter.")
        }
    }

    companion object {
        private const val TAG = "NotebookFragment"
    }
}