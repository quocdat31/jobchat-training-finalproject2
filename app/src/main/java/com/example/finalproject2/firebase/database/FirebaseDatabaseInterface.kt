package com.example.finalproject2.firebase.database

import com.example.finalproject2.model.Message
import com.example.finalproject2.model.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface FirebaseDatabaseInterface {
    //sign up
    fun createUser(id: String, name: String, email: String): Completable

    //add friend
    fun addFriend(friendEmail: String): Completable

    fun getFriendList(): Observable<List<User>>
    fun findFriendByEmail(friendEmail: String): Single<User>

    //chat
    fun createConversation(memberList: ArrayList<String>): Single<String>
    fun sendMessage(conversationId: String, message: Message): Completable
    fun getMessageList(conversationId: String): Observable<List<Message>>
    fun findConversation(friendEmail: String): Single<String>
}
