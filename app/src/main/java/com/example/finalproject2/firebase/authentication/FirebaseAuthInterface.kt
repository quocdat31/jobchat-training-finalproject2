package com.example.finalproject2.firebase.authentication

import io.reactivex.Completable

interface FirebaseAuthInterface {
    fun signUp(email: String, password: String, username: String): Completable
    fun getUserId(): String
    fun getUsername(): String
    fun getUserEmail(): String
    fun logout()
    fun signIn(email: String, password: String): Completable
}
