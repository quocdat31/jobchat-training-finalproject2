package com.example.finalproject2.ultis.extension

import android.view.View

fun View.disable() {
    this.isEnabled = false
}

fun View.enable() {
    this.isEnabled = true
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.isGone() = visibility == View.GONE

inline fun View.onStateChange(crossinline onStateChangeHandler: (Int) -> Unit) {
    this.tag = this.visibility
    viewTreeObserver.addOnGlobalLayoutListener {
        val newState = this.visibility
        if (tag != newState) {
            this.tag = this.visibility
            onStateChangeHandler(newState)
        }
    }
}
