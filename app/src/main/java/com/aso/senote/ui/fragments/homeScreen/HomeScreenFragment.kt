/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.ui.fragments.homeScreen

import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.aso.senote.EventObserver
import com.aso.senote.R
import com.aso.senote.data.PreferenceRepository
import com.aso.senote.databinding.FragmentHomeScreenBinding
import com.aso.senote.ui.activities.MainActivity
import com.aso.senote.ui.fragments.addEditNote.AddEditNoteFragmentArgs
import com.aso.senote.ui.fragments.notebook.NotebookFragmentArgs
import com.aso.senote.util.ToolbarIconClickListener
import com.aso.senote.util.getPreferencesRepository
import com.aso.senote.util.getViewModelFactory
import com.aso.senote.widget.MaterialMenuDrawable
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform

class HomeScreenFragment : Fragment() {


    private val mViewModel by viewModels<HomeScreenViewModel> { getViewModelFactory() }

    private lateinit var preferencesRepository: PreferenceRepository

    private lateinit var binding: FragmentHomeScreenBinding

    private lateinit var mainNavController: NavController

    private lateinit var materialMenuDrawable: MaterialMenuDrawable


    private var notebookId: Long = -1
    private var notebookIdLive: Long = -1
    private var currentFragmentId = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentHomeScreenBinding.inflate(
                inflater, container, false).apply {
            viewModel = mViewModel
            addBackdrop.viewModel = mViewModel
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preferencesRepository = getPreferencesRepository()
        mainNavController = findNavController()
        preferencesRepository.defaultNotebookIdLive.observe(viewLifecycleOwner, Observer {
            notebookIdLive = it
        })

        setupTopToolbar()
        setupInsertMenu()
        setupNavigation()
        setupAccordingToSubNavFragment()
    }

    private fun setupTopToolbar() {

        materialMenuDrawable = MaterialMenuDrawable(requireContext(),
                ContextCompat.getColor(requireContext(), R.color.colorOnPrimary),
                MaterialMenuDrawable.Stroke.THIN)

        val config = requireActivity().resources.configuration
        if (config.layoutDirection == View.LAYOUT_DIRECTION_RTL)
            materialMenuDrawable.setRTLEnabled(true)
        else materialMenuDrawable.setRTLEnabled(false)


        binding.appBar.setNavigationOnClickListener(ToolbarIconClickListener(
                requireContext(),
                binding.navHostFrame,
                AccelerateDecelerateInterpolator(),
                binding.backdropOverlay))
        materialMenuDrawable = MaterialMenuDrawable(requireContext(),
                ContextCompat.getColor(requireContext(), R.color.colorOnPrimary),
                MaterialMenuDrawable.Stroke.THIN)
        binding.appBar.navigationIcon = materialMenuDrawable
    }

    private fun setupNavigation() {
        mViewModel.insert.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                HomeScreenViewModel.Companion.Type.TEXT -> {
                    val args = AddEditNoteFragmentArgs.Builder(MainActivity.Action.ADD)
                            .setNotebookId(notebookId)
                            .build()
                            .toBundle()
                    mainNavController.navigate(R.id.action_homeScreenFragment_to_addEditNoteFragment, args)

                }
                HomeScreenViewModel.Companion.Type.TODO_LIST -> {
                }
                HomeScreenViewModel.Companion.Type.PICTURE -> {
                }
                HomeScreenViewModel.Companion.Type.ATTACHMENT -> {
                }
                HomeScreenViewModel.Companion.Type.VOICE_NOTE -> {
                }
                HomeScreenViewModel.Companion.Type.HAND_WRITING -> {
                }
            }
        })
    }

    private fun setupInsertMenu() {
        mViewModel.showInsertMenu.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                expandToInsertMenu()
            } else {
                collapseToAddFab()
            }
        })
    }


    /**
     * Getting the notebook id to add note to.
     * Change the TopToolbar Menu
     * chang the backDrop menu.
     * */
    private fun setupAccordingToSubNavFragment() {

        val subNavController = Navigation.findNavController(requireActivity(),
                R.id.sub_fragment_container)
        subNavController.addOnDestinationChangedListener { _: NavController?,
                                                           destination: NavDestination,
                                                           arguments: Bundle? ->
            currentFragmentId = destination.id
            ToolbarIconClickListener.currentFragmentId = currentFragmentId

            when (destination.id) {
                R.id.notebookFragment -> {
                    materialMenuDrawable.animateIconState(MaterialMenuDrawable.IconState.ARROW)
                    binding.appBar.menu.clear()
                    binding.appBar.inflateMenu(R.menu.top_notebook_fragment_menu)
                    binding.appBar.setOnMenuItemClickListener {
                        false
                    }
                    notebookId = if (arguments != null) NotebookFragmentArgs.fromBundle(arguments).notebookId else -1
                }

                R.id.notebooksFragment -> {
                    materialMenuDrawable.animateIconState(MaterialMenuDrawable.IconState.BURGER)
                    binding.appBar.menu.clear()
                    binding.appBar.inflateMenu(R.menu.top_home_screen_fragment_menu)
                    binding.appBar.setOnMenuItemClickListener {
                        false
                    }
                    notebookId = notebookIdLive
                }
            }
        }
    }

    private fun expandToInsertMenu() {
        val transform = MaterialContainerTransform(requireContext()).apply {
            startView = binding.addNote
            endView = binding.addCard

            pathMotion = MaterialArcMotion()

            duration = 300

            scrimColor = Color.TRANSPARENT
        }

        TransitionManager.beginDelayedTransition(binding.parent, transform)
        binding.addNote.visibility = View.GONE
        binding.addCard.visibility = View.VISIBLE
        binding.addOverlay.visibility = View.VISIBLE
    }

    private fun collapseToAddFab() {
        val transform = MaterialContainerTransform(requireContext()).apply {
            endView = binding.addNote
            startView = binding.addCard

            pathMotion = MaterialArcMotion()

            duration = 300

            scrimColor = Color.TRANSPARENT
        }

        TransitionManager.beginDelayedTransition(binding.parent, transform)
        binding.addNote.visibility = View.VISIBLE
        binding.addCard.visibility = View.GONE
        binding.addOverlay.visibility = View.GONE
    }

}
