package com.example.finalproject2.ultis

import android.view.View

fun View.disable() {
    this.isEnabled = false
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}
