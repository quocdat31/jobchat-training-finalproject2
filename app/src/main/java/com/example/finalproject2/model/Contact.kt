package com.example.finalproject2.model

import java.io.Serializable

data class Contact(var conversationId: String? = null): User(), Serializable
