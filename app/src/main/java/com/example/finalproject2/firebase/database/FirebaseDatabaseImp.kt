package com.example.finalproject2.firebase.database

import android.util.Log
import com.example.finalproject2.firebase.authentication.FirebaseAuthImpl
import com.example.finalproject2.model.Message
import com.example.finalproject2.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

object FirebaseDatabaseImp :
    FirebaseDatabaseInterface {
    private const val CHILD_USER_PATH = "users"
    private const val CHILD_USER_FRIEND_PATH = "friends"
    private const val CHILD_EMAIL = "email"
    const val DOES_NOT_EXISTS_ERROR = "does not exists"
    private const val CONVERSATIONS_PATH = "conversations"
    private const val MESSAGES_PATH = "messages"
    private const val CHILD_CONTACT_PATH = "contacts"
    private const val CHILD_ID_PATH = "id"
    private const val CHILD_MEMBER_LIST = "memberList"

    private val mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference
    private val mFirebaseAuth = FirebaseAuth.getInstance()

    override fun createUser(
        id: String,
        name: String,
        email: String
    ): Completable {
        return Completable.create { emitter ->
            val user = User(id, name, email)
            mFirebaseDatabaseReference.child(CHILD_USER_PATH).child(id).setValue(user)
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

    @Suppress("UNCHECKED_CAST")
    override fun getFriendList(): Observable<List<User>> {
        return Observable.create { emitter ->
            mFirebaseDatabaseReference.child(CHILD_CONTACT_PATH).child(FirebaseAuthImpl.getUserId())
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(dataSnapshot: DatabaseError) {
                        emitter.onError(dataSnapshot.toException())
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val friendList = dataSnapshot.children.map { childDataSnapshot ->
                            childDataSnapshot.getValue(User::class.java)
                        }
                        emitter.onNext(friendList as List<User>)
                    }
                })
        }
    }

    override fun findFriendByEmail(friendEmail: String): Single<User> {
        val query =
            mFirebaseDatabaseReference.child(CHILD_USER_PATH)
                .orderByChild(CHILD_EMAIL)
                .equalTo(friendEmail)
        return Single.create { emitter ->
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {
                    emitter.onError(Throwable(dataSnapshot.message))
                }
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (childDataSnapshot in dataSnapshot.children) {
                            val data = childDataSnapshot.getValue(User::class.java)
                            if (data != null) {
                                val user = User().apply {
                                    id = data.id
                                    name = data.name
                                    email = data.email
                                }
                                emitter.onSuccess(user)
                            }
                        }
                    } else emitter.onError(Throwable("$friendEmail $DOES_NOT_EXISTS_ERROR"))
                }
            })
        }
    }

    override fun createConversation(memberList: ArrayList<String>): Single<String> {
        return Single.create { emitter ->
            val conversationId = mFirebaseDatabaseReference.child(CONVERSATIONS_PATH).push().key
            mFirebaseDatabaseReference.child(CONVERSATIONS_PATH).child(conversationId.toString())
                .setValue(conversationId.toString())
                .addOnCompleteListener { task ->

                    //users/conversation/
                    if (task.isSuccessful && task.isComplete) {
                        mFirebaseDatabaseReference.child(CHILD_USER_PATH)
                            .child(FirebaseAuthImpl.getUserId()).child(
                                CONVERSATIONS_PATH
                            ).child(conversationId.toString()).child(CHILD_ID_PATH)
                            .setValue(conversationId.toString())
                        emitter.onSuccess(conversationId.toString())

                        //conversations/memberList
                        val memberId = mFirebaseDatabaseReference.child(CONVERSATIONS_PATH).child(
                            CHILD_MEMBER_LIST
                        ).push().key
                        for (member in memberList) {
                            mFirebaseDatabaseReference.child(CONVERSATIONS_PATH)
                                .child(CHILD_MEMBER_LIST).child(memberId.toString()).setValue(member)
                        }

                    }
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    override fun sendMessage(conversationId: String, message: Message): Completable {
        return Completable.create { emitter ->
            val key =
                mFirebaseDatabaseReference.child(MESSAGES_PATH).child(conversationId).push().key
            mFirebaseDatabaseReference.child(MESSAGES_PATH).child(conversationId)
                .child(key.toString()).setValue(message)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.isComplete) emitter.onComplete()
                }
                .addOnFailureListener { exception -> emitter.onError(exception) }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun getMessageList(conversationId: String): Observable<List<Message>> {
        return Observable.create { emitter ->
            mFirebaseDatabaseReference.child(MESSAGES_PATH).child(conversationId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) = emitter.onError(p0.toException())
                    override fun onDataChange(p0: DataSnapshot) {
                        val messageList = p0.children.map { childDataSnapshot ->
                            childDataSnapshot.getValue(Message::class.java)
                        }
                        emitter.onNext(messageList as List<Message>)
                    }
                })
        }
    }

    override fun findConversation(friendEmail: String): Single<String> {
        val query =
            mFirebaseDatabaseReference.child(CONVERSATIONS_PATH).orderByChild(CHILD_MEMBER_LIST)
                .equalTo(friendEmail)
        return Single.create { emitter ->
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) = emitter.onError(p0.toException())
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        emitter.onSuccess(p0.value.toString())
                        Log.d("asd", "success: ${p0.value.toString()}")
                    }
                    Log.d("asd", p0.toString())
                }
            })
        }
    }

    override fun addFriend(friendEmail: String): Completable {
        val friendReference =
            mFirebaseDatabaseReference.child(CHILD_CONTACT_PATH).child(FirebaseAuthImpl.getUserId())
                .push()
        return Completable.create { emitter ->
            findFriendByEmail(friendEmail).subscribe { user, error ->
                friendReference.setValue(user).addOnCompleteListener { task ->
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

}
