/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.ui.fragments.notebook

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aso.senote.EventObserver
import com.aso.senote.R
import com.aso.senote.databinding.FragmentNotebookBinding
import com.aso.senote.ui.activities.MainActivity
import com.aso.senote.util.getViewModelFactory

/**
 * Display a grid of [Note]s for a specific [Notebook] using its Id.
 */
class NotebookFragment : Fragment() {
    private val mViewModel by viewModels<NotebookViewModel> { getViewModelFactory() }

    private val args: NotebookFragmentArgs by navArgs()

    private lateinit var binding: FragmentNotebookBinding

    private lateinit var listAdapter: NotesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.move)
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
        setupTopToolbar()
        setupListAdapter()
        setupNavigation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startPostponedEnterTransition()
    }

    private fun setupNavigation() {
        mViewModel.openNoteEvent.observe(viewLifecycleOwner, EventObserver {
            openNote(it);
        })
    }

    private fun openNote(noteId: Long) {
        val action = NotebookFragmentDirections.actionNotebookFragmentToAddEditNoteFragment(MainActivity.Action.PREVIEW)
                .setNoteId(noteId)
        findNavController().navigate(action)

    }

    private fun setupListAdapter() {
        val viewModel = binding.viewModel
        if (viewModel != null) {
            listAdapter = NotesAdapter(viewModel)
            binding.notebookRv.adapter = listAdapter
        } else {
            Log.w(TAG, "ViewModel not initialized when attempting to setup adapter.")
        }
    }

    private fun setupTopToolbar() {
        binding.notebookToolbar.apply {
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_attach ->
                        true
                    R.id.action_lists_preferences ->
                        true
                    R.id.action_menu ->
                        true
                    else ->
                        false
                }
            }
        }

    }

    companion object {
        private const val TAG = "NotebookFragment"
    }
}