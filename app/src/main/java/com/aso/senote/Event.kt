/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote

import androidx.lifecycle.Observer


/** Created by ahmedsomer on 4/5/20.
 *
 ****SeNote
 *
 */

/**
 * Used as wrapper for data that is exposed via a LiveData that represents an Event.
 * */

class Event<out T>(private val content: T) {
    var hasBeenHandled = false
        private set //Allow external read but not write.

    /**
     * Returns the content and prevents its use again.
     * */
    fun getContentIfNotHandled(): T? {

        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     * */
    fun peekContent(): T = content

}

/**
 * An [Observer] for [Event]s , simplifying the pattern of checking if the [Event]'s
 * content has already been handled.
 *
 * [onEventUnHandledContent] is *only* called if the [Event]'s content has not been handled.
 * */

class EventObserver<T>(private val onEventUnHandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(t: Event<T>?) {
        t?.getContentIfNotHandled()?.let {
            onEventUnHandledContent(it)
        }
    }

}