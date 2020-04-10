/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.ui.fragments.addEditNote

import android.os.Bundle
import android.util.TypedValue
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aso.senote.R
import com.aso.senote.databinding.FragmentAddEditNoteBinding
import com.aso.senote.util.KeyboardUtils
import com.aso.senote.util.dpToPx
import com.aso.senote.util.getViewModelFactory

class AddEditNoteFragment : Fragment() {

    private lateinit var topMenu: Menu

    /*  private lateinit var starredMenuItem: MenuItem
      private lateinit var unStarredMenuItem: MenuItem*/

    private var isInEditMode: Boolean = false

    private lateinit var binding: FragmentAddEditNoteBinding

    private val args: AddEditNoteFragmentArgs by navArgs()

    private val mViewModel by viewModels<AddEditNoteViewModel> { getViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (isInEditMode) {
                mViewModel.cancel()
            } else {
                findNavController().navigateUp()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddEditNoteBinding.inflate(
                inflater, container, false).apply {
            viewModel = mViewModel
            editorPreferencesPanel.viewModel = mViewModel
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        //Set the lifecycle owner to the lifecycle of the view
        binding.lifecycleOwner = this.viewLifecycleOwner

        //setupToast()
        mViewModel.start(args.noteId, args.notebookId, args.addOrPreview)
        setupTopToolbar()
        setupEditMode()
        setupNavigation()
        setupPreferencesPanel()
/*        setupBottomToolbar()*/
    }

    private fun setupTopToolbar() {
        topMenu = binding.toolbar.menu

        binding.toolbar.apply {
            inflateMenu(R.menu.top_note_fragment_menu)
            setNavigationOnClickListener {
                if (isInEditMode) {
                    mViewModel.cancel()
                } else {
                    findNavController().navigateUp()
                }
            }
            setOnMenuItemClickListener { item: MenuItem? ->
                when (item?.itemId) {
                    R.id.action_edit -> {
                        mViewModel.isEditMode.value = true
                        true
                    }
                    R.id.action_save -> {
                        mViewModel.saveNote()
                        true
                    }
                    R.id.action_more ->
                        true
                    else -> false
                }
            }
        }
    }

    /* private fun setupBottomToolbar() {
         requireActivity().bottom_app_bar.apply {
             inflateMenu(R.menu.bottom_note_fragment_menu)
             setOnMenuItemClickListener { item: MenuItem? ->
                 when (item?.itemId) {
                     R.id.action_change_paper -> {

                         true
                     }

                     R.id.action_share_note_fragment -> {

                         true
                     }
                     R.id.action_add_star -> {
                         mViewModel.setStarred(true)
                         true
                     }
                     R.id.action_remove_star -> {
                         mViewModel.setStarred(false)
                         true
                     }

                     else -> false
                 }
             }
         }

         starredMenuItem = requireActivity().bottom_app_bar.menu.findItem(R.id.action_remove_star)
         unStarredMenuItem = requireActivity().bottom_app_bar.menu.findItem(R.id.action_add_star)

     }*/

    private fun setupEditMode() {
        mViewModel.isEditMode.observe(viewLifecycleOwner, Observer {
            isInEditMode = it
            if (it) {
                topMenu.findItem(R.id.action_save).isVisible = true
                topMenu.findItem(R.id.action_edit).isVisible = false
                topMenu.findItem(R.id.action_more).isVisible = false
//                requireActivity().bottom_app_bar.performHide()
                /*requireActivity().add_note.hide()*/
            } else {
                if (mViewModel.isNoteEmpty()) {
                    findNavController().navigateUp()
                } else {
                    topMenu.findItem(R.id.action_save).isVisible = false
                    topMenu.findItem(R.id.action_edit).isVisible = true
                    topMenu.findItem(R.id.action_more).isVisible = true
                    KeyboardUtils.forceCloseKeyboard(binding.root)
//                    requireActivity().add_note.show()
//                    requireActivity().bottom_app_bar.performShow()
                }
            }
        })

        mViewModel.isStarred.observe(viewLifecycleOwner, Observer {
            /*  if (it) {
                  starredMenuItem.isVisible = false
                  unStarredMenuItem.isVisible = true
              } else {
                  starredMenuItem.isVisible = true
                  unStarredMenuItem.isVisible = false
              }*/
        })
    }

    private fun setupPreferencesPanel() {
        mViewModel.lineSpacing.observe(viewLifecycleOwner, Observer {
            binding.contentInput.setLineSpacing(it.toFloat(), 1.0f)
            binding.noteTitle.setLineSpacing(it.toFloat(), 1.0f)
        })

        mViewModel.leftMargin.observe(viewLifecycleOwner, Observer {

            binding.contentInput.setPadding(
                    dpToPx(it, requireContext()), binding.contentInput.paddingTop,
                    binding.contentInput.paddingRight,
                    binding.contentInput.paddingBottom)

            binding.noteTitle.setPadding(
                    dpToPx(it, requireContext()), binding.noteTitle.paddingTop,
                    binding.noteTitle.paddingLeft,
                    binding.noteTitle.paddingBottom)
        })

        mViewModel.rightMargin.observe(viewLifecycleOwner, Observer {
            binding.contentInput.setPadding(binding.contentInput.paddingLeft,
                    binding.contentInput.paddingTop, dpToPx(it, requireContext()),
                    binding.contentInput.paddingBottom)

            binding.noteTitle.setPadding(
                    binding.noteTitle.paddingLeft,
                    binding.noteTitle.paddingTop, dpToPx(it, requireContext()),
                    binding.noteTitle.paddingBottom)
        })

        mViewModel.textSize.observe(viewLifecycleOwner, Observer {
            binding.contentInput.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                    it.toFloat(), requireContext().resources.displayMetrics)
            binding.noteTitle.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                    it.toFloat(), requireContext().resources.displayMetrics)
        })
    }

    private fun setupNavigation() {

    }

    private fun setupToast() {
    }

    companion object {
        private const val TAG = "AddEditNoteFragment"
    }
}