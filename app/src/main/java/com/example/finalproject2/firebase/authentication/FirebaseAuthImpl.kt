package com.example.finalproject2.firebase.authentication

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import io.reactivex.Completable

object FirebaseAuthImpl : FirebaseAuthInterface {
    private val mFirebaseAuth = FirebaseAuth.getInstance()
    override fun signUp(
        email: String,
        password: String,
        username: String
    ): Completable {
        return Completable.create { emitter ->
            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isComplete && task.isSuccessful) {
                        mFirebaseAuth.currentUser?.updateProfile(
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

    override fun getUserId(): String = mFirebaseAuth.currentUser?.uid.toString()

    override fun getUsername(): String = mFirebaseAuth.currentUser?.displayName.toString()

    override fun getUserEmail(): String = mFirebaseAuth.currentUser?.email.toString()

    override fun logout() {
        mFirebaseAuth.signOut()
    }

    override fun signIn(
        email: String,
        password: String
    ): Completable {
        return Completable.create { emitter ->
            mFirebaseAuth.signInWithEmailAndPassword(email, password)
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
