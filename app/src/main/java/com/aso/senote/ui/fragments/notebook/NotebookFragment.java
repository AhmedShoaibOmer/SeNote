/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.ui.fragments.notebook;

import android.content.Context;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.aso.senote.R;
import com.aso.senote.databinding.FragmentNotebookBinding;
import com.aso.senote.ui.fragments.homescreen.HomeScreenViewModel;
import com.google.android.material.appbar.MaterialToolbar;


public class NotebookFragment extends Fragment {

    private FragmentNotebookBinding binding;
    private Context mContext;
    private NotesAdapter adapter = new NotesAdapter(null);
    private HomeScreenViewModel homeScreenViewModel;
    private NotebookViewModel notebookViewModel;

    public NotebookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSharedElementEnterTransition(TransitionInflater.from(mContext).inflateTransition(android.R.transition.move));
        setSharedElementReturnTransition(TransitionInflater.from(mContext).inflateTransition(android.R.transition.move));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_notebook, container, false);
        //Setting Data to the layout
        //binding.setData(data);
        //TransitionInflater.from(mContext).inflateTransition(android.R.transition.explode);

        postponeEnterTransition();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startPostponedEnterTransition();

        initToolbar();
        init();
        initBottomToolbar();
    }

    private void initBottomToolbar() {

    }

    private void initToolbar() {
        MaterialToolbar toolbar = binding.notebookToolbar;
        toolbar.setNavigationOnClickListener(v -> {
            NavHostFragment.findNavController(NotebookFragment.this).navigateUp();
        });
        toolbar.inflateMenu(R.menu.top_notebook_fragment_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            return false;
        });
    }

    private void init() {

        RecyclerView notebooksRecyclerView = binding.notebookRv;
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        notebooksRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        notebooksRecyclerView.setAdapter(adapter);


    }
/*
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeScreenViewModel = new ViewModelProvider(this).get(HomeScreenViewModel.class);
        notebookViewModel = new ViewModelProvider(this).get(NotebookViewModel.class);
        homeScreenViewModel.getSelectedNotebookWithNotes().observe(getViewLifecycleOwner(), notebookWithNotes -> {
            adapter.submitList(notebookWithNotes.getNotes());
            binding.setNotebook(notebookWithNotes.getNotebook());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClicked(Note item) {
        notebookViewModel.setSelectedNote(item);
        NavHostFragment.findNavController(NotebookFragment.this)
                .navigate(R.id.action_notebookFragment_to_addEditNoteFragment);
    }*/
}
