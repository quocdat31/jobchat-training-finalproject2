package com.example.finalproject2.firebase

object FirebaseConstant {
    object Child {
        const val CONTACT = "contacts"
        const val USERS = "users"
        const val CONVERSATIONS = "conversations"

        const val MESSAGES = "$CONVERSATIONS/messages"
        const val LAST_MESSAGE = "$CONVERSATIONS/lastMessage"
    }
}
