package com.aso.senote.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.aso.senote.R;
import com.aso.senote.databinding.FragmentHomeBinding;
import com.aso.senote.util.PhotoAttachmentHandler;

public class HomeFragment extends Fragment {


    private FragmentHomeBinding binding;
    private PhotoAttachmentHandler mPhotoAttachmentHandler;
    private View mView;
    private Context mContext;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_home, container, false);
        //Setting Data to the layout
        //binding.setData(data);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        init();
    }

    private void init() {
        EditText editor = binding.testET;

        mPhotoAttachmentHandler = new PhotoAttachmentHandler(editor);
        binding.testFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoAttachmentHandler.insert(" :-) ", R.drawable.test);
            }
        });
    }


}
