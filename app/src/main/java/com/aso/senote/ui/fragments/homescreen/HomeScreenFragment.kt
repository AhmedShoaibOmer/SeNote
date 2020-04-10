/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.ui.fragments.homescreen

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
import com.aso.senote.databinding.FragmentHomeScreenBinding
import com.aso.senote.util.getViewModelFactory
import kotlinx.android.synthetic.main.list_item_notebook.*


/**
 * Display a grid of [Notebook]s.
 * */
class HomeScreenFragment : Fragment() {

    private val mViewModel by viewModels<HomeScreenViewModel> { getViewModelFactory() }

    private lateinit var binding: FragmentHomeScreenBinding

    private lateinit var listAdapter: NotebooksAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeScreenBinding.inflate(
                inflater, container, false).apply {
            viewModel = mViewModel
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Set the lifecycle owner to the lifecycle of the view
        binding.lifecycleOwner = this.viewLifecycleOwner
        setupTopToolbar()
        setupListAdapter()
        setupNavigation()
    }

    private fun setupNavigation() {
        mViewModel.openNotebookEvent.observe(viewLifecycleOwner, EventObserver {
            openNotebook(it)
        })
    }

    private fun openNotebook(notebookId: Long) {
        val extras: FragmentNavigator.Extras = FragmentNavigator.Extras.Builder().addSharedElement(
                notebook_title_tv,
                "notebook_text_view"
        ).build()
        val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToNotebookFragment(notebookId)
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

    private fun setupTopToolbar() {
        binding.notebooksToolbar.apply {
            setNavigationOnClickListener {
                //TODO Open The NavDrawer.
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_add_notebook -> {
                        mViewModel.createNotebook(
                                Notebook(getString(R.string.untitled),
                                        null)
                        )
                        true
                    }
                    R.id.action_search ->
                        true
                    else -> false
                }
            }
        }
    }

    /*

    private void init() {

        RecyclerView notebooksRecyclerView = binding.homeScreenRv;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        notebooksRecyclerView.setLayoutManager(gridLayoutManager);
        notebooksRecyclerView.setAdapter(adapter);


    }
*/

    /* private void initToolbar() {
        MaterialToolbar actionBarToolBar = binding.notebooksToolbar;
        actionBarToolBar.setNavigationOnClickListener(v -> {

        });
        actionBarToolBar.inflateMenu(R.menu.home_screen_fragment_menu);
        final Menu menu = actionBarToolBar.getMenu();
        actionBarToolBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.app_bar_search:
                    return true;
                case R.id.action_add_notebook:
                    homeScreenViewModel.insertNotebook(new Notebook("Notebook", null));
                    return true;
                default:
                    return false;
            }
        });
    }*/
    companion object {
        private const val TAG = "HomeScreenFragment"
    }
}