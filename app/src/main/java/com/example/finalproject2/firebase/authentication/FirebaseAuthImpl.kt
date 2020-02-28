package com.example.finalproject2.firebase.authentication

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Completable
import io.reactivex.Single
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

object FirebaseAuthImpl : FirebaseAuthInterface {

    private val mFirebaseAuth = FirebaseAuth.getInstance()
    private val formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm")

    private fun setConnectivityState(connectivityState: String) {
        FirebaseDatabase
            .getInstance()
            .reference
            .child("users")
            .child(getUserId())
            .child("connectivityState")
            .setValue(connectivityState)
    }

    override fun signUp(
        email: String,
        password: String,
        username: String,
        imageUri: Uri
    ): Single<String> {
        return Single.create { emitter ->
            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { createUserTask ->
                    if (createUserTask.isComplete && createUserTask.isSuccessful) {
                        mFirebaseAuth.currentUser?.updateProfile(
                            UserProfileChangeRequest
                                .Builder()
                                .setDisplayName(username)
                                .setPhotoUri(imageUri)
                                .build()
                        )?.addOnCompleteListener { updateProfileTask ->
                            if (updateProfileTask.isComplete && updateProfileTask.isSuccessful)
                                emitter.onSuccess(getUserId())
                        }
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
    override fun getUserImageUri(): String = mFirebaseAuth.currentUser?.photoUrl.toString()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun logout() {
        setConnectivityState("Last seen ${LocalDateTime.now().format(formatter)}")
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
                        setConnectivityState("Online")
                    }
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

}
