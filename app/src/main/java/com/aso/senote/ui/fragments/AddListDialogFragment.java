/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aso.senote.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddListDialogFragment extends BottomSheetDialogFragment {

    public static AddListDialogFragment newInstance() {
        return new AddListDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_list_dialog_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      /*  NavigationView navigationView = view.findViewById(R.id.addnav);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.action_add_photo:
                    return true;
                case R.id.action_attach_file:
                    return true;
                case R.id.action_todo_list:
                    return true;
                default:
                    return false;
            }
        });*/
    }


}
