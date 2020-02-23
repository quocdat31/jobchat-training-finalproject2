package com.example.finalproject2.firebase.database

import com.example.finalproject2.model.Friend
import com.example.finalproject2.model.FriendResponse
import com.example.finalproject2.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import javax.inject.Inject


class FirebaseDatabaseImp @Inject constructor(private var firebaseDatabase: FirebaseDatabase) :
    FirebaseDatabaseInterface {

    override fun createUser(
        id: String,
        name: String,
        email: String
    ): Observable<Void> {
        return Observable.create { emitter ->
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

    override fun getFriendList(id: String): Observable<List<Friend>> {

        return Observable.create { emitter ->

            firebaseDatabase.reference.child(CHILD_USER_PATH).child(id)
                .child(CHILD_USER_FRIEND_PATH)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        emitter.onError(p0.toException())
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val friendList = ArrayList<Friend>()
                        for (item in p0.children) {
                            val friend = item.getValue(Friend::class.java)
                            if (friend != null) {
                                friendList.add(friend)
                            }
                        }
                        emitter.onNext(friendList)
                    }
                })
        }
    }

    override fun addFriend(
        id: String,
        friendEmail: String
    ): Observable<Void> {

        val friendReference = firebaseDatabase.reference.child(CHILD_USER_PATH).child(id)
            .child(CHILD_USER_FRIEND_PATH).push()
        val query =
            firebaseDatabase.reference.child(CHILD_USER_PATH).orderByChild(CHILD_EMAIL)
                .equalTo(friendEmail)
        return Observable.create { emitter ->
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) = Unit
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (dataSnapshot in p0.children) {
                            val data = dataSnapshot.getValue(FriendResponse::class.java)
                            if (data != null) {
                                val friend = Friend(
                                    id = data.id,
                                    name = data.name,
                                    email = data.email,
                                    status = ADD_FRIEND_REQUEST_STATUS
                                )
                                friendReference.setValue(friend).addOnCompleteListener { task ->
                                    if (task.isComplete && task.isSuccessful)
                                        emitter.onComplete()
                                }
                                    .addOnFailureListener { exception ->
                                        emitter.onError(exception)
                                    }
                            }
                        }
                    } else {
                        emitter.onError(Exception("$friendEmail $DOES_NOT_EXISTS_ERROR"))
                    }
                }
            })
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
