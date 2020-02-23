package com.example.finalproject2.model

data class SignUpRequest(
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var confirmPassword: String? = null
)
