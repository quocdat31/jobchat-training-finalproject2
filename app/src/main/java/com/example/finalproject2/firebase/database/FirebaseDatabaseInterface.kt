package com.example.finalproject2.firebase.database

import com.example.finalproject2.model.Friend
import com.example.finalproject2.model.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface FirebaseDatabaseInterface {
    fun createUser(id: String, name: String, email: String): Completable
    fun addFriend(id: String, friendEmail: String): Completable
    fun getFriendList(id: String): Observable<List<Friend>>
    fun findFriendByEmail(friendEmail: String): Single<Friend>
}
