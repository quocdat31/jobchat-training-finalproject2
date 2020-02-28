package com.example.finalproject2.model

import java.io.Serializable

open class User(
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var imageUri: String? = null,
    var connectivityState: String? = null
) : Serializable
