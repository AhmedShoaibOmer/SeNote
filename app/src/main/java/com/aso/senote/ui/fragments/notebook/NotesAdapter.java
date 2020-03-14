/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.ui.fragments.notebook;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.aso.senote.R;
import com.aso.senote.data.models.Note;
import com.aso.senote.databinding.ListItemNoteGridBinding;

import java.util.List;

// Created by ahmedsomer on 3/29/20.
//
///***SeNote
//
//

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteVH> {
    private AsyncListDiffer<Note> mDiffer;

    private NotebookViewModel viewModel;

    //DefineAsyncListDiffer
    NotesAdapter(NotebookViewModel viewModel) {
        //Callback
        DiffUtil.ItemCallback<Note> diffCallback = new DiffUtil.ItemCallback<Note>() {
            @Override
            public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                return oldItem.equals(newItem);
            }
        };
        mDiffer = new AsyncListDiffer<>(this, diffCallback);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public NoteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //DataBinding class
        ListItemNoteGridBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_item_note_grid, parent, false);
        return new NoteVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteVH holder, int position) {
        Note currentItem = mDiffer.getCurrentList().get(position);
        holder.bind(viewModel, currentItem);
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    //Method to submit List
    void submitList(List<Note> data) {
        mDiffer.submitList(data);
    }

    static class NoteVH extends RecyclerView.ViewHolder {
        ListItemNoteGridBinding binding;

        NoteVH(@NonNull ListItemNoteGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(NotebookViewModel viewModel, Note currentItem) {
            binding.setViewModel(viewModel);
            binding.setNote(currentItem);
        }
    }
}
