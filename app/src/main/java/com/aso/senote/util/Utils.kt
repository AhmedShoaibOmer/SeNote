/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.util

import android.content.Context
import android.graphics.Bitmap
import android.widget.TextView
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.Swatch

/* Created by ahmedsomer on 3/29/20.
 *
 ***SeNote
 *
 */

fun dpToPx(dp: Int, context: Context): Int {
    val scale: Float = context.resources.displayMetrics.density
    return (dp * scale + 0.5F).toInt()
}


private fun getMostPopularSwatch(palette: Palette?): Swatch? {
    var mostPopular: Swatch? = null
    if (palette != null) {
        for (swatch in palette.swatches) {
            if (mostPopular == null || swatch.population > mostPopular.population) mostPopular = swatch
        }
    }
    return mostPopular
}


fun setNotebookTextColor(bitmap: Bitmap?, title: TextView) {
    if (bitmap != null && !bitmap.isRecycled) {
        Palette.from(bitmap)
                .generate { palette: Palette? ->
                    val mostPopular = getMostPopularSwatch(palette)
                    if (mostPopular != null) {
                        title.setBackgroundColor(mostPopular.rgb)
                        // TODO: 3/29/20 try title text color
                        title.setTextColor(mostPopular.titleTextColor)
                    }
                }
    }
}