package com.example.finalproject2.model

import android.net.Uri

data class SignUpRequest(
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var imageUri: Uri? = null,
    var confirmPassword: String? = null
)
