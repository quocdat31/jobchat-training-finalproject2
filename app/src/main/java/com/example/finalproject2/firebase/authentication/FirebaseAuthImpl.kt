package com.example.finalproject2.firebase.authentication

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class FirebaseAuthImpl @Inject constructor(val firebaseAuth: FirebaseAuth): FirebaseAuthInterface {

    override fun signUp(
        email: String,
        password: String,
        username: String
    ): Completable {
        return Completable.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isComplete && task.isSuccessful) {
                        firebaseAuth.currentUser?.updateProfile(
                            UserProfileChangeRequest
                                .Builder()
                                .setDisplayName(username)
                                .build()
                        )
                        emitter.onComplete()
                    }
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    override fun getUserId(): String = firebaseAuth.currentUser?.uid.toString()

    override fun getUsername(): String = firebaseAuth.currentUser?.displayName.toString()

    override fun getUserEmail(): String = firebaseAuth.currentUser?.email.toString()

    override fun logout() = firebaseAuth.signOut()

    override fun signIn(
        email: String,
        password: String
    ): Completable {
        return Completable.create { emitter ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isComplete && task.isSuccessful) {
                        emitter.onComplete()
                    }
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }
}
