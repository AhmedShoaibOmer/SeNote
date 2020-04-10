/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.Interpolator
import android.widget.ImageButton
import androidx.navigation.findNavController
import com.aso.senote.R
import com.aso.senote.ui.activities.MainActivity
import com.aso.senote.widget.MaterialMenuDrawable

/** Created by ahmedsomer on 4/11/20.
 *
 ****SeNote
 *
 */


/**
 * [android.view.View.OnClickListener] used to translate the NavHost grid sheet downward on
 * the Y-axis when the navigation icon, the search icon or the menu icon in the toolbar is pressed.
 */
class ToolbarIconClickListener @JvmOverloads internal constructor(
        private val context: Context, private val sheet: View,
        private val interpolator: Interpolator? = null,
        private val backdropOverlay: View?
) : View.OnClickListener {

    private val animatorSet = AnimatorSet()
    private val height: Int
    private var backdropShown = false

    init {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
    }

    override fun onClick(view: View) {

        if (currentFragmentId == R.id.notebooksFragment) {

            backdropShown = !backdropShown

            // Cancel the existing animations
            animatorSet.removeAllListeners()
            animatorSet.end()
            animatorSet.cancel()

            updateIcon(view)

            val translateY: Int = height - context.resources.getDimensionPixelSize(R.dimen.notebook_grid_reveal_height)


            val animator = ObjectAnimator.ofFloat(sheet, "translationY", (if (backdropShown) translateY else 0).toFloat())
            animator.duration = 300
            if (interpolator != null) {
                animator.interpolator = interpolator
            }
            animatorSet.play(animator)
            animator.start()
        } else {
            (context as MainActivity).findNavController(R.id.sub_fragment_container).navigateUp()
        }
    }

    private fun updateIcon(view: View) {
        val icon = ((view as ImageButton).drawable as MaterialMenuDrawable)
        if (backdropShown) {
            icon.animateIconState(MaterialMenuDrawable.IconState.X)
            backdropOverlay!!.visibility = View.VISIBLE
        } else {
            icon.animateIconState(MaterialMenuDrawable.IconState.BURGER)
            backdropOverlay!!.visibility = View.GONE
        }
    }

    companion object {
        @JvmStatic
        var currentFragmentId: Int = -1
    }

}
