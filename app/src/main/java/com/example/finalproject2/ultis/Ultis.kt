package com.example.finalproject2.ultis

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap

object Ultis {
    fun getExtension(uri: Uri, context: Context): String {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(context.contentResolver.getType(uri)).toString()
    }
}

fun String.toUri(): Uri {
    return Uri.parse(this)
}
