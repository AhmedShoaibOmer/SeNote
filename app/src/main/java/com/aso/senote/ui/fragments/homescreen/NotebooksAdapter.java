/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.ui.fragments.homescreen;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.aso.senote.R;
import com.aso.senote.data.models.Notebook;
import com.aso.senote.databinding.ListItemNotebookBinding;

import java.util.List;

// Created by ahmedsomer on 3/19/20.
//
///***SeNote
//
//

public class NotebooksAdapter extends RecyclerView.Adapter<NotebooksAdapter.NotebookVH> {
    private final HomeScreenViewModel viewModel;
    private AsyncListDiffer<Notebook> mDiffer;

    //DefineAsyncListDiffer
    NotebooksAdapter(HomeScreenViewModel viewModel) {
        //Callback
        DiffUtil.ItemCallback<Notebook> diffCallback = new DiffUtil.ItemCallback<Notebook>() {
            @Override
            public boolean areItemsTheSame(@NonNull Notebook oldItem, @NonNull Notebook newItem) {
                return oldItem.getNotebookId() == newItem.getNotebookId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Notebook oldItem, @NonNull Notebook newItem) {
                return oldItem.equals(newItem);
            }
        };
        mDiffer = new AsyncListDiffer<>(this, diffCallback);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public NotebookVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //DataBinding class
        ListItemNotebookBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.list_item_notebook, parent, false);
        return new NotebookVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotebookVH holder, int position) {
        Notebook currentItem = mDiffer.getCurrentList().get(position);
        holder.bind(viewModel, currentItem);
    }

    @Override
    public int getItemCount() {
        return mDiffer.getCurrentList().size();
    }

    //Method to submit List
    void submitList(List<Notebook> data) {
        mDiffer.submitList(data);
    }


    static class NotebookVH extends RecyclerView.ViewHolder {
        private ListItemNotebookBinding binding;

        NotebookVH(@NonNull ListItemNotebookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(HomeScreenViewModel viewModel, Notebook item) {

            binding.setViewModel(viewModel);
            binding.setNotebook(item);
        }
    }
}
