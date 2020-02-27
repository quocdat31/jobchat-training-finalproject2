package com.example.finalproject2.firebase.database

import android.util.Log
import com.example.finalproject2.firebase.authentication.FirebaseAuthImpl
import com.example.finalproject2.model.Message
import com.example.finalproject2.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
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
            mFirebaseDatabaseReference
                .child(CHILD_USER_PATH)
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

//    override fun createConversation(conversationId: String, friendId: String): Completable {
//        return Completable.create { emitter ->
//
//            val hashMap = hashMapOf<String, Any>()
//            val key = mFirebaseDatabaseReference.child(CONVERSATIONS_PATH).push().key
//            val currentUserId = FirebaseAuthImpl.getUserId()
//            val currentUserPath =
//                "$CHILD_USER_PATH/$currentUserId/$CONVERSATIONS_PATH/$key/$CHILD_ID_PATH"
//            val friendPath =
//                "$CHILD_USER_PATH/$friendId/$CONVERSATIONS_PATH/$key/$CHILD_ID_PATH"
//
//            hashMap.put(currentUserPath, conversationId)
//            hashMap.put(friendPath, conversationId)
//
//            mFirebaseDatabaseReference.updateChildren(hashMap)
//                .addOnCompleteListener { task ->
//                    if (task.isComplete && task.isSuccessful) {
//                        emitter.onComplete()
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    emitter.onError(exception)
//                }
//
//        }
//    }

//    override fun createConversation(memberList: ArrayList<String>): Single<String> {
//        return Single.create { emitter ->
//            val conversationId = mFirebaseDatabaseReference.child(CONVERSATIONS_PATH).push().key
//            mFirebaseDatabaseReference.child(CONVERSATIONS_PATH).child(conversationId.toString())
//                .child(CHILD_ID_PATH)
//                .setValue(conversationId.toString())
//                .addOnCompleteListener { task ->
//
//                    //users/conversation/
//                    for (memberId in memberList) {
//                        if (task.isSuccessful && task.isComplete) {
//                            mFirebaseDatabaseReference.child(CHILD_USER_PATH)
//                                .child(memberId).child(
//                                    CONVERSATIONS_PATH
//                                ).child(conversationId.toString()).child(CHILD_ID_PATH)
//                                .setValue(conversationId.toString())
//                            emitter.onSuccess(conversationId.toString())
//                        }
//                    }
//
//                    //conversations/memberList
//                    mFirebaseDatabaseReference.child(CONVERSATIONS_PATH)
//                        .child(conversationId.toString())
//                        .child(CHILD_MEMBER_LIST).setValue(memberList)
//
//                }
//                .addOnFailureListener { exception ->
//                    emitter.onError(exception)
//                }
//        }
//    }

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
                .addListenerForSingleValueEvent(object : ValueEventListener {
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

    override fun accessConversation(user: User): Single<String> {
        return Single.create { emitter ->
            mFirebaseDatabaseReference
                .child(CHILD_USER_PATH)
                .child(user.id.toString())
                .child(CONVERSATIONS_PATH)
                .orderByChild(CHILD_ID_PATH)
                .equalTo(FirebaseAuthImpl.getUserId())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        Log.d("asd", p0.toString())
                    }
            })
        }
    }

    override fun startListeningMessageListChange(conversationId: String): Single<Message> {
        return Single.create { emitter ->
            mFirebaseDatabaseReference.child(MESSAGES_PATH).child(conversationId)
                .addChildEventListener(object : ChildEventListener {
                    override fun onCancelled(p0: DatabaseError) = emitter.onError(p0.toException())
                    override fun onChildMoved(p0: DataSnapshot, p1: String?) = Unit
                    override fun onChildChanged(p0: DataSnapshot, p1: String?) = Unit
                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        emitter.onSuccess(p0.getValue(Message::class.java)!!)
                    }

                    override fun onChildRemoved(p0: DataSnapshot) = Unit
                })
        }
    }

    override fun addFriend(friendEmail: String): Completable {
        val contactKey =
            mFirebaseDatabaseReference
                .child(CHILD_CONTACT_PATH)
                .child(FirebaseAuthImpl.getUserId())
                .push().key.toString()
        //current user add friend
        return Completable.create { emitter ->
            findFriendByEmail(friendEmail)
                .subscribe { user, error ->
                    mFirebaseDatabaseReference.child(CHILD_CONTACT_PATH)
                        .child(FirebaseAuthImpl.getUserId())
                        .child(contactKey)
                        .setValue(user)
                        .addOnCompleteListener { task ->
                            if (task.isComplete && task.isSuccessful) {
                                //friend add current user
                                val currentUser = User(
                                    id = FirebaseAuthImpl.getUserId(),
                                    name = FirebaseAuthImpl.getUserEmail(),
                                    email = FirebaseAuthImpl.getUserEmail()
                                )
                                mFirebaseDatabaseReference.child(CHILD_CONTACT_PATH)
                                    .child(user.id.toString()).child(contactKey)
                                    .setValue(currentUser).addOnCompleteListener { task2 ->
                                        if (task2.isSuccessful && task2.isComplete) {
                                            emitter.onComplete()
                                        }
                                    }.addOnFailureListener { exception ->
                                        emitter.onError(exception)
                                    }
                            } else {
                                emitter.onError(error)
                            }
                }
                    .addOnFailureListener { exception ->
                        emitter.onError(exception)
                    }
            }
        }
    }
}
