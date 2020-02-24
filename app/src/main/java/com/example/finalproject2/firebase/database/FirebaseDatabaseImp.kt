package com.example.finalproject2.firebase.database

import com.example.finalproject2.model.Friend
import com.example.finalproject2.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject


class FirebaseDatabaseImp @Inject constructor(private var firebaseDatabase: FirebaseDatabase) :
    FirebaseDatabaseInterface {

    override fun createUser(
        id: String,
        name: String,
        email: String
    ): Completable {
        return Completable.create { emitter ->
            val user = User(id, name, email)
            firebaseDatabase.reference.child(CHILD_USER_PATH).child(id).setValue(user)
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

    override fun getFriendList(id: String): Single<List<Friend>> {

        return Single.create { emitter ->
            firebaseDatabase.reference.child(CHILD_USER_PATH).child(id)
                .child(CHILD_USER_FRIEND_PATH)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(dataSnapshot: DatabaseError) {
                        emitter.onError(dataSnapshot.toException())
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val friendList = dataSnapshot.children.map { childDataSnapshot ->
                            childDataSnapshot.getValue(Friend::class.java)
                        }
                        emitter.onSuccess(friendList as List<Friend>)
                    }
                })
        }
    }

    override fun findFriendByEmail(friendEmail: String): Single<Friend> {
        val query =
            firebaseDatabase.reference.child(CHILD_USER_PATH).orderByChild(CHILD_EMAIL)
                .equalTo(friendEmail)
        return Single.create { emitter ->
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                    emitter.onError(Throwable(dataSnapshot.message))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (childDataSnapshot in dataSnapshot.children) {
                            val data = childDataSnapshot.getValue(Friend::class.java)
                            if (data != null) {
                                val friend = Friend().apply {
                                    id = data.id
                                    name = data.name
                                    email = data.email
                                    status = ADD_FRIEND_REQUEST_STATUS
                                }
                                emitter.onSuccess(friend)
                            }
                        }
                    } else emitter.onError(Throwable("$friendEmail $DOES_NOT_EXISTS_ERROR"))
                }
            })
        }
    }

    override fun addFriend(id: String, friendEmail: String): Completable {
        val friendReference = firebaseDatabase.reference.child(CHILD_USER_PATH).child(id)
            .child(CHILD_USER_FRIEND_PATH).push()
        return Completable.create { emitter ->
            findFriendByEmail(friendEmail).subscribe { friend, error ->
                friendReference.setValue(friend).addOnCompleteListener { task ->
                    if (task.isComplete && task.isSuccessful)
                        emitter.onComplete()
                }
                    .addOnFailureListener { exception ->
                        emitter.onError(exception)
                    }
                emitter.onError(error)
            }
        }
    }

    companion object {
        const val CHILD_USER_PATH = "users"
        const val CHILD_USER_FRIEND_PATH = "friends"
        const val CHILD_EMAIL = "email"
        const val ADD_FRIEND_REQUEST_STATUS = "Waiting for response"
        const val DOES_NOT_EXISTS_ERROR = "does not exists"
    }
}
