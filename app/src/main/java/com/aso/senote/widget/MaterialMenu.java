/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.widget;

/**
 * Created by ahmedsomer on 4/14/20.
 * <p>
 * ***SeNote
 */

import android.animation.Animator;
import android.view.animation.Interpolator;

/**
 * API for interaction with {@link MaterialMenuDrawable}
 */
public interface MaterialMenu {
    /**
     * Return current icon state
     *
     * @return icon state
     */
    MaterialMenuDrawable.IconState getIconState();

    /**
     * Change icon without animation
     *
     * @param state new icon state
     */
    void setIconState(MaterialMenuDrawable.IconState state);

    /**
     * Animate icon to given state.
     *
     * @param state new icon state
     */
    void animateIconState(MaterialMenuDrawable.IconState state);

    /**
     * Set color of icon
     *
     * @param color new icon color
     */
    void setColor(int color);

    /**
     * Set visibility of icon
     *
     * @param visible new value for visibility
     */
    void setVisible(boolean visible);

    /**
     * Set duration of transformation animations
     *
     * @param duration new animation duration
     */
    void setTransformationDuration(int duration);

    /**
     * Set interpolator for transformation animations
     *
     * @param interpolator new interpolator
     */
    void setInterpolator(Interpolator interpolator);

    /**
     * Set listener for {@code MaterialMenuDrawable} animation events
     *
     * @param listener new listener or null to remove any listener
     */
    void setAnimationListener(Animator.AnimatorListener listener);

    /**
     * Enable RTL layout. Flips all icons horizontally
     *
     * @param rtlEnabled true to enable RTL layout
     */
    void setRTLEnabled(boolean rtlEnabled);

    /**
     * Manually set a transformation value for an {@link MaterialMenuDrawable.AnimationState}
     *
     * @param animationState state to set value in
     * @param value          between {@link MaterialMenuDrawable#TRANSFORMATION_START} and
     *                       {@link MaterialMenuDrawable#TRANSFORMATION_END}.
     */
    MaterialMenuDrawable.IconState setTransformationOffset(MaterialMenuDrawable.AnimationState animationState, float value);
}