package com.example.finalproject2.ultis.extension

import android.content.Context
import android.widget.Toast
import com.example.finalproject2.R

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()