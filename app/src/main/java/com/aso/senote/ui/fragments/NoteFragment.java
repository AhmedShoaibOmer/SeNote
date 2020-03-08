/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.aso.senote.R;
import com.aso.senote.databinding.FragmentNoteBinding;
import com.aso.senote.editor.SEditor;
import com.aso.senote.util.KeyboardUtils;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment {

    private FragmentNoteBinding binding;
    private Context mContext;
    private SEditor editor;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //handles the onBackPressed();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (!editor.handleOnBackPressed()) {
                    NavHostFragment.findNavController(NoteFragment.this).navigateUp();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public NoteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_note, container, false);
        //Setting Data to the layout
        //binding.setData(data);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editor = new SEditor(mContext, view, binding.parent, binding.contentInput,
                binding.noteTitle, binding.copy, binding.bottomAppBar, binding.toolbar);

        editor.startPreviewMode();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        editor = null;
        KeyboardUtils.removeAllKeyboardToggleListeners();
    }

}