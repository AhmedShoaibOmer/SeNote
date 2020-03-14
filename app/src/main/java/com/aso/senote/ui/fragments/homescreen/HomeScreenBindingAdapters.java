/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.ui.fragments.homescreen;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.aso.senote.R;
import com.aso.senote.data.models.Notebook;
import com.aso.senote.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;
import java.util.Objects;

/**
 * [BindingAdapter]'s for the [Notebook]'s list.
 */
public class HomeScreenBindingAdapters {

    @BindingAdapter("items")
    public static void setItems(RecyclerView recyclerView, List<Notebook> items) {
        ((NotebooksAdapter) Objects.requireNonNull(recyclerView.getAdapter())).submitList(items);
    }

    @BindingAdapter({"loadImageFromUrl", "bindTextTitle"})
    public static void loadImage(ImageView imageView, String Url, TextView title) {

        Glide.with(imageView.getContext())
                .asBitmap()
                .load(Url)
                .placeholder(imageView.getContext().getResources().getDrawable(R.drawable.cover_sample))
                .addListener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        Bitmap resource = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        Utils.setNotebookTextColor(resource, title);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        Utils.setNotebookTextColor(resource, title);
                        return false;
                    }
                })
                .into(imageView);

    }
}
