package com.example.finalproject2.firebase.database

import com.example.finalproject2.model.Friend
import com.example.finalproject2.model.User
import io.reactivex.Observable

interface FirebaseDatabaseInterface {
    fun createUser(id: String, name: String, email: String): Observable<Void>
    fun addFriend(id: String, friendEmail: String): Observable<Void>
    fun getFriendList(id: String): Observable<List<Friend>>
}
