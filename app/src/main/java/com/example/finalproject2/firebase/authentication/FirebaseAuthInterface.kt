package com.example.finalproject2.firebase.authentication

import android.net.Uri
import io.reactivex.Completable
import io.reactivex.Single

interface FirebaseAuthInterface {
    fun signUp(email: String, password: String, username: String, imageUri: Uri): Single<String>
    fun getUserId(): String
    fun getUsername(): String
    fun getUserEmail(): String
    fun getUserImageUri(): String
    fun logout()
    fun signIn(email: String, password: String): Completable
}
