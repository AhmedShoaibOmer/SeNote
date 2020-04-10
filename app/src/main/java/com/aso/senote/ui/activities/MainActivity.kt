/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.ui.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation.findNavController
import com.aso.senote.R
import com.aso.senote.databinding.ActivityMainBinding
import com.aso.senote.ui.fragments.addEditNote.AddEditNoteFragmentArgs
import com.aso.senote.ui.fragments.notebook.NotebookFragmentArgs
import com.google.android.material.bottomappbar.BottomAppBar

/**
 * Main Activity for the app.
 * Holds The Navigation Host Fragment and the Drawer, bottom Toolbar, etc.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var preferences: SharedPreferences

    private lateinit var navController: NavController

    private var notebookId: Long = -1

    private var currentFragmentId = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
         *
         *Sets the Color of the status bar if on an android Lollipop device.
         *
         * */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(applicationContext,
                    R.color.colorPrimaryDark)
        }
        preferences = getSharedPreferences(
                resources.getString(R.string.PREFERENCES_FILE_KEY),
                Context.MODE_PRIVATE)

        //Set the bottom toolbar as main toolbar;
        setSupportActionBar(binding.bottomAppBar)

        /*
         *Handling the fab drawable resource for each fragment and getting the
         *notebook id to add note to.
         * */navController = findNavController(this@MainActivity,
                R.id.fragment_container)
        navController.addOnDestinationChangedListener { _: NavController?,
                                                        destination: NavDestination,
                                                        arguments: Bundle? ->
            currentFragmentId = destination.id

            when (destination.id) {
                R.id.addEditNoteFragment -> {
                    // changing the navigation Linear layout visibility.
                    binding.bottomNavigation.root.visibility = View.GONE
                    setupFabAsCopyFab()
                }
                R.id.notebookFragment -> {

                    // changing the navigation Linear layout visibility.
                    binding.bottomNavigation.root.visibility = View.VISIBLE
                    binding.bottomAppBar.menu.clear()
                    setupFabAsAddNoteFab()
                    notebookId = if (arguments != null) NotebookFragmentArgs.fromBundle(arguments).notebookId else -1
                }

                R.id.homeScreenFragment -> {
                    // changing the navigation Linear layout visibility.
                    binding.bottomNavigation.root.visibility = View.VISIBLE
                    binding.bottomAppBar.menu.clear()
                    setupFabAsAddNoteFab()
                    notebookId = preferences.getLong(resources
                            .getString(R.string.DEFAULT_NOTEBOOK_ID), -1)
                }
            }
        }
    }

    private fun setupFabAsAddNoteFab() {
        binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        binding.addNote.setImageResource(R.drawable.ic_note_add_black_24dp)
    }

    private fun setupFabAsCopyFab() {
        binding.addNote.setImageResource(R.drawable.ic_note_fragment_copy_24dp)
        binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
    }

    fun addOrCopyNote(view: View?) {
        if (currentFragmentId == R.id.addEditNoteFragment) {

            // TODO: 3/30/20 Implement copy
            /* ClipboardManager manager = ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE));
            ClipData clipData = ClipData.newPlainText(,);
            manager.setPrimaryClip(clipData);*/
        } else {
            val args = AddEditNoteFragmentArgs.Builder(Action.ADD)
                    .setNotebookId(notebookId)
                    .build()
                    .toBundle()
            if (currentFragmentId == R.id.homeScreenFragment)
                navController.navigate(R.id.action_homeScreenFragment_to_addEditNoteFragment, args)
            else
                navController.navigate(R.id.action_notebookFragment_to_addEditNoteFragment, args)
        }
    }

    fun handWriting(view: View?) {}

    fun addTodoList(view: View?) {}

    fun addPhotoNote(view: View?) {}

    fun addVoiceNote(view: View?) {}

    enum class Action {
        PREVIEW, ADD
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}