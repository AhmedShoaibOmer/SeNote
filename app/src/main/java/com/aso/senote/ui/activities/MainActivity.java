/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.ui.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.aso.senote.R;
import com.aso.senote.databinding.ActivityMainBinding;
import com.aso.senote.ui.fragments.addEditNote.AddEditNoteFragmentArgs;
import com.aso.senote.ui.fragments.notebook.NotebookFragmentArgs;
import com.google.android.material.bottomappbar.BottomAppBar;

/**
 * Main Activity for the app.
 * Holds The Navigation Host Fragment and the Drawer, bottom Toolbar, etc.
 */

public class MainActivity extends AppCompatActivity
        /*implements AddEditNoteFragment.BottomToolbarListener*/ {


    private ActivityMainBinding binding;
    private SharedPreferences preferences;
    private NavController navController;
    private long notebookId;
    private int currentFragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*
         *
         *Sets the Color of the status bar if on an android Lollipop device.
         *
         * */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),
                    R.color.colorPrimaryDark));
        }

        preferences = getSharedPreferences(
                getResources().getString(R.string.PREFERENCES_FILE_KEY),
                Context.MODE_PRIVATE);

        //Set the bottom toolbar as main toolbar;
        setSupportActionBar(binding.bottomAppBar);

        /*
         *Handling the fab drawable resource for each fragment and getting the
         *notebook id to add note to.
         * */
        navController = Navigation.findNavController(MainActivity.this,
                R.id.fragment_container);
        navController.addOnDestinationChangedListener((controller, destination,
                                                       arguments) -> {
            currentFragmentId = destination.getId();
            switch (destination.getId()) {
                case R.id.addEditNoteFragment:
                    // changing the navigation Linear layout visibility.
                    binding.navigationLl.setVisibility(View.GONE);
                    setupFabAsCopyFab();
                    break;
                case R.id.notebookFragment:
                    // changing the navigation Linear layout visibility.
                    binding.navigationLl.setVisibility(View.VISIBLE);
                    setupFabAsAddNoteFab();
                    if (arguments != null)
                        notebookId = NotebookFragmentArgs.fromBundle(arguments).getNotebookId();
                    else notebookId = -1;
                    break;
                case R.id.homeScreenFragment:
                    // changing the navigation Linear layout visibility.
                    binding.navigationLl.setVisibility(View.VISIBLE);
                    setupFabAsAddNoteFab();
                    notebookId = preferences.getLong(getResources()
                            .getString(R.string.DEFAULT_NOTEBOOK_ID), -1);
                    break;
            }

        });
    }

    private void setupFabAsAddNoteFab() {
        binding.bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        binding.addNote.setImageResource(R.drawable.ic_note_add_black_24dp);
    }

    private void setupFabAsCopyFab() {
        binding.addNote.setImageResource(R.drawable.ic_note_fragment_copy_24dp);
        binding.bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
    }

    public void addOrCopyNote(View view) {
        if (currentFragmentId == R.id.addEditNoteFragment) {

            // TODO: 3/30/20 Implement copy
           /* ClipboardManager manager = ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE));
            ClipData clipData = ClipData.newPlainText(,);
            manager.setPrimaryClip(clipData);*/

        } else {
            Bundle args = new AddEditNoteFragmentArgs.Builder(Action.ADD)
                    .setNotebookId(notebookId)
                    .build()
                    .toBundle();
            if (currentFragmentId == R.id.homeScreenFragment)
                navController.navigate(R.id.action_homeScreenFragment_to_addEditNoteFragment, args);
            else
                navController.navigate(R.id.action_notebookFragment_to_addEditNoteFragment, args);

        }
    }

    public void handWriting(View view) {
    }

    public void addTodoList(View view) {
    }

    public void addPhotoNote(View view) {
    }

    public void addVoiceNote(View view) {
    }

    public enum Action {
        PREVIEW,
        ADD
    }
}
