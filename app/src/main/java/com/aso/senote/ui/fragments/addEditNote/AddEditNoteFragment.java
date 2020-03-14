/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.ui.fragments.addEditNote;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aso.senote.databinding.FragmentNoteBinding;
import com.aso.senote.editor.SEditor;
import com.aso.senote.ui.fragments.notebook.NotebookViewModel;
import com.aso.senote.util.KeyboardUtils;

import org.jetbrains.annotations.NotNull;

public class AddEditNoteFragment extends Fragment /*implements SEditor.NoteListener, SEditor.BottomToolbarMenuListener */ {
    private static final String TAG = "AddEditNoteFragment";

    private FragmentNoteBinding binding;
    private NotebookViewModel notebookViewModel;
    private Context mContext;
    private SEditor editor;

    public AddEditNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*notebookViewModel = new ViewModelProvider(this).get(NotebookViewModel.class);
        notebookViewModel.getBottomToolbarListener().observe(getViewLifecycleOwner(), bottomToolbarListener -> {
            editor.setBottomToolbarListener(bottomToolbarListener);
        });
        notebookViewModel.setBottomToolbarMenuListener(this);
        notebookViewModel.getSelectedNote().observe(getViewLifecycleOwner(), note -> {
            editor.setNote(note);
            if (note.getNoteTitle().isEmpty() && note.getNoteContent().isEmpty()) {
                editor.startEditMode();
            } else
                editor.startPreviewMode();
            Log.d(TAG, "onCreateView: note observed");
        });*/

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* //handles the onBackPressed();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (!editor.handleOnBackPressed()) {
                    NavHostFragment.findNavController(AddEditNoteFragment.this).navigateUp();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);*/
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNoteBinding.inflate(
                inflater, container, false);
        //Setting Data to the layout
        //binding.setData(data);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       /* editor = new SEditor(mContext, binding, this);
        editor.initializeEditor();*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        editor = null;
        KeyboardUtils.removeAllKeyboardToggleListeners();
    }

   /* @Override
    public boolean saveNote(Note note, boolean isNewNote) {
        if (isNewNote) {
            notebookViewModel.insertNote(note);
            return false;
        } else
            notebookViewModel.updateNote(note);
        return false;

    }

    @Override
    public void togglePreferencesMenu() {
        editor.toggleShowPreferences();
    }

    @Override
    public void setStarred(boolean isStarred) {
        Note note = editor.getNote();
        note.setStarred(isStarred);
        notebookViewModel.updateNote(note);
    }

    public interface BottomToolbarListener {

        void toggleVisibility(boolean isInEditMode);
        void setStarred(boolean isStarred);
    }

*/
}