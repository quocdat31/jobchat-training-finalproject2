package com.example.finalproject2.model

data class Friend(
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var status: String? = null
)

data class FriendResponse(
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var friendList: List<Friend>? = null
)
