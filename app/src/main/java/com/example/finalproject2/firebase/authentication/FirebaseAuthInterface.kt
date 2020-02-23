package com.example.finalproject2.firebase.authentication

import io.reactivex.Observable

interface FirebaseAuthInterface {
    fun signUp(email: String, password: String, username: String): Observable<Void>
    fun getUserId(): String
    fun getUsername(): String
    fun getUserEmail(): String
    fun logout()
    fun signIn(email: String, password: String): Observable<Void>
}
