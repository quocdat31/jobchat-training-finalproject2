package com.example.finalproject2.firebase.database

import com.example.finalproject2.model.Contact
import com.example.finalproject2.model.Message
import com.example.finalproject2.model.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface FirebaseDatabaseInterface {
    //sign up
    fun createUser(id: String, name: String, email: String, imageUri: String): Completable

    //add friend
    fun addFriend(friendEmail: String): Completable
    fun getFriendList(): Observable<List<Contact>>
    fun findFriendByEmail(friendEmail: String): Single<User>

    //conversation
    fun sendMessage(conversationId: String, message: Message): Completable
    fun isEmptyMessageList(conversationId: String): Single<Boolean>
    fun getMessageList(conversationId: String): Single<List<Message>>
    fun getMessage(conversationId: String, onEmptyMessage: () -> Unit): Observable<Message>

    //chat
    fun getConversation(): Observable<String>
    fun getConversationList(): Observable<List<String>>

    fun findFriendById(friendId: String): Observable<User>
    fun getlastMessageOfConversation(
        conversationId: String,
        onEmptyChat: () -> Unit?
    ): Observable<Message>

}
