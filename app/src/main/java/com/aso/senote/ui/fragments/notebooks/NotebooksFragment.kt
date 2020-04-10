/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.ui.fragments.notebooks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.aso.senote.EventObserver
import com.aso.senote.R
import com.aso.senote.data.models.Notebook
import com.aso.senote.databinding.FragmentNotebooksBinding
import com.aso.senote.util.getViewModelFactory
import kotlinx.android.synthetic.main.list_item_notebook.*


/**
 * Display a grid of [Notebook]s.
 * */
class NotebooksFragment : Fragment() {

    private val mViewModel by viewModels<NotebooksViewModel> { getViewModelFactory() }

    private lateinit var binding: FragmentNotebooksBinding

    private lateinit var listAdapter: NotebooksAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotebooksBinding.inflate(
                inflater, container, false).apply {
            viewModel = mViewModel
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Set the lifecycle owner to the lifecycle of the view
        binding.lifecycleOwner = this.viewLifecycleOwner
        setupListAdapter()
        setupNavigation()
        binding.addNotebook.setOnClickListener {
            mViewModel.createNotebook(
                    Notebook(getString(R.string.untitled),
                            null))
        }
    }

    private fun setupNavigation() {
        mViewModel.openNotebookEvent.observe(viewLifecycleOwner, EventObserver {
            openNotebook(it)
        })
    }

    private fun openNotebook(notebookId: Long) {
        val extras: FragmentNavigator.Extras = FragmentNavigator.Extras.Builder().addSharedElement(
                notebook_title_tv,
                "notebook"
        ).build()
        val action = NotebooksFragmentDirections.actionNotebooksFragmentToNotebookFragment(notebookId)
        findNavController(this).navigate(action, extras)
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewModel
        if (viewModel != null) {
            listAdapter = NotebooksAdapter(viewModel)
            binding.homeScreenRv.adapter = listAdapter
        } else {
            Log.w(TAG, "ViewModel not initialized when attempting to setup adapter.")
        }
    }

    companion object {
        private const val TAG = "NotebooksFragment"
    }
}