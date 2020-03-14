/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.util;
// Created by ahmedsomer on 3/29/20.
//
///***SeNote
//
//

import android.graphics.Bitmap;
import android.widget.TextView;

import androidx.palette.graphics.Palette;

public class Utils {

    private static Palette.Swatch getMostPopularSwatch(Palette palette) {
        Palette.Swatch mostPopular = null;
        if (palette != null) {
            for (Palette.Swatch swatch : palette.getSwatches()) {
                if (mostPopular == null || swatch.getPopulation() > mostPopular.getPopulation())
                    mostPopular = swatch;
            }
        }
        return mostPopular;
    }

    public static void setNotebookTextColor(Bitmap bitmap, TextView title) {

        if (bitmap != null && !bitmap.isRecycled()) {
            Palette.from(bitmap)
                    .generate(palette -> {
                        Palette.Swatch mostPopular = getMostPopularSwatch(palette);
                        if (mostPopular != null) {
                            title.setBackgroundColor(mostPopular.getRgb());
                            // TODO: 3/29/20 try title text color
                            title.setTextColor(mostPopular.getTitleTextColor());
                        }
                    });
        }
    }
}
