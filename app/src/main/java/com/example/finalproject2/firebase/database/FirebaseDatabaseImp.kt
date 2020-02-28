package com.example.finalproject2.firebase.database

import android.net.Uri
import com.example.finalproject2.firebase.authentication.FirebaseAuthImpl
import com.example.finalproject2.model.Contact
import com.example.finalproject2.model.Message
import com.example.finalproject2.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface MessageListener {
    fun onChange(message: Message)
}

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
    private const val CHILD_CONVERSATION_ID = "conversationId"
    private const val CHILD_LAST_MESSAGE = "lastMessage"

    lateinit var mMessageListener: MessageListener

    private val mFirebaseDatabaseReference = FirebaseDatabase.getInstance().reference
    private val mFirebaseAuth = FirebaseAuth.getInstance()
    private val mFirebaseStorage = FirebaseStorage.getInstance().reference

    fun setMessageListener(messageListener: MessageListener?) {
        if (messageListener != null) {
            this.mMessageListener = messageListener
        }
    }

    fun uploadImage(imageUri: Uri): Single<String> {
        return Single.create { emitter ->
            val fileRef =
                mFirebaseStorage.child("users").child(System.currentTimeMillis().toString())
            fileRef.putFile(imageUri)
                .addOnSuccessListener {
                    fileRef.downloadUrl.addOnCompleteListener { task: Task<Uri> ->
                        if (task.isSuccessful && task.isComplete) {
                            emitter.onSuccess(task.result.toString())
                        }
                    }
                }
        }
    }

    override fun createUser(
        id: String,
        name: String,
        email: String,
        imageUri: String
    ): Completable {
        return Completable.create { emitter ->
            val user = User(id, name, email, imageUri)
            mFirebaseDatabaseReference
                .child(CHILD_USER_PATH)
                .child(id).setValue(user)
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
    override fun getFriendList(): Observable<List<Contact>> {
        return Observable.create { emitter ->
            mFirebaseDatabaseReference
                .child(CHILD_CONTACT_PATH)
                .child(FirebaseAuthImpl.getUserId())
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(dataSnapshot: DatabaseError) {
                        emitter.onError(dataSnapshot.toException())
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val friendList = dataSnapshot.children.map { childDataSnapshot ->
                            childDataSnapshot.getValue(Contact::class.java)
                        }
                        emitter.onNext(friendList as List<Contact>)
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
                                    imageUri = data.imageUri
                                }
                                emitter.onSuccess(user)
                            }
                        }
                    } else
                        emitter.onError(Throwable("$friendEmail $DOES_NOT_EXISTS_ERROR"))
                }
            })
        }
    }

    override fun findFriendById(friendId: String): Observable<User> {
        val query = mFirebaseDatabaseReference
            .child(CHILD_USER_PATH)
            .orderByChild(CHILD_ID_PATH)
            .equalTo(friendId)
        return Observable.create { emitter ->
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) = emitter.onError(p0.toException())
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        p0.children.map { userData ->
                            val user = userData.getValue(User::class.java)
                            if (user != null) {
                                emitter.onNext(user)
                            }
                        }
                    }
                }
            })
        }
    }

    override fun sendMessage(conversationId: String, message: Message): Completable {
        val hashmap = hashMapOf<String, Message>()
        val lastMessagePath = "$CONVERSATIONS_PATH/$CHILD_LAST_MESSAGE/$conversationId"

        val messageKey =
            mFirebaseDatabaseReference
                .child(CONVERSATIONS_PATH)
                .child(MESSAGES_PATH)
                .child(conversationId)
                .push().key
                .toString()

        val messagePath = "$CONVERSATIONS_PATH//$MESSAGES_PATH/$conversationId/$messageKey"
        message.id = messageKey

        hashmap.put(lastMessagePath, message)
        hashmap.put(messagePath, message)

        return Completable.create { emitter ->
            mFirebaseDatabaseReference
                .updateChildren(hashmap as Map<String, Any>)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.isComplete)
                        emitter.onComplete()
                    mFirebaseDatabaseReference
                        .child(CONVERSATIONS_PATH)
                        .child(CHILD_LAST_MESSAGE)
                        .child(conversationId)
                        .child("conversationId")
                        .setValue(conversationId)
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

//    override fun sendMessage(conversationId: String, message: Message): Completable {
//        return Completable.create { emitter ->
//            val key =
//                mFirebaseDatabaseReference
//                    .child(MESSAGES_PATH)
//                    .child(conversationId).push().key
//            message.id = key
//            mFirebaseDatabaseReference
//                .child(MESSAGES_PATH)
//                .child(conversationId)
//                .child(key.toString())
//                .setValue(message)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful && task.isComplete) emitter.onComplete()
//                }
//                .addOnFailureListener { exception -> emitter.onError(exception) }
//        }
//    }

    override fun isEmptyMessageList(conversationId: String): Single<Boolean> {
        return Single.create { emitter ->
            mFirebaseDatabaseReference.child(MESSAGES_PATH).orderByKey().equalTo(conversationId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) = emitter.onError(p0.toException())
                    override fun onDataChange(p0: DataSnapshot) {
                        if (!p0.exists()) emitter.onSuccess(true) else emitter.onSuccess(false)
                    }
                })
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun getMessageList(conversationId: String): Single<List<Message>> {
        return Single.create { emitter ->
            mFirebaseDatabaseReference
                .child(CONVERSATIONS_PATH)
                .child(MESSAGES_PATH)
                .child(conversationId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) = emitter.onError(p0.toException())
                    override fun onDataChange(p0: DataSnapshot) {
                        val messageList = p0.children.map { childDataSnapshot ->
                            childDataSnapshot.getValue(Message::class.java)
                        }
                        emitter.onSuccess(messageList as List<Message>)
                    }
                })
        }
    }

    override fun getMessage(
        conversationId: String,
        onEmptyMessage: () -> Unit
    ): Observable<Message> {
        return Observable.create { emitter ->
            val messageReference = mFirebaseDatabaseReference
                .child(CONVERSATIONS_PATH)
                .child(MESSAGES_PATH)
            messageReference
                .child(conversationId)
                .addChildEventListener(object : ChildEventListener {
                    override fun onCancelled(p0: DatabaseError) =
                        emitter.onError(p0.toException())

                    override fun onChildMoved(p0: DataSnapshot, p1: String?) = Unit
                    override fun onChildChanged(p0: DataSnapshot, p1: String?) = Unit
                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        if (!p0.hasChildren()) onEmptyMessage()
                        else
                            if (p0.key != CHILD_LAST_MESSAGE) {
                                val message = p0.getValue(Message::class.java)
                                if (message != null) emitter.onNext(message)
                            }
                    }

                    override fun onChildRemoved(p0: DataSnapshot) = Unit
                })
        }
    }

    override fun getConversation(): Observable<String> {
        return Observable.create { emitter ->
            mFirebaseDatabaseReference
                .child(CHILD_CONTACT_PATH)
                .child(FirebaseAuthImpl.getUserId())
                .addChildEventListener(object : ChildEventListener {
                    override fun onCancelled(p0: DatabaseError) = emitter.onError(p0.toException())
                    override fun onChildMoved(p0: DataSnapshot, p1: String?) = Unit
                    override fun onChildChanged(p0: DataSnapshot, p1: String?) = Unit
                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        emitter.onNext(p0.key.toString())
                    }

                    override fun onChildRemoved(p0: DataSnapshot) = Unit
                })
        }
    }

    override fun getConversationList(): Observable<List<String>> {
        return Observable.create { emitter ->
            mFirebaseDatabaseReference
                .child(CHILD_CONTACT_PATH)
                .child(FirebaseAuthImpl.getUserId())
                .addChildEventListener(object : ChildEventListener {
                    override fun onCancelled(p0: DatabaseError) = emitter.onError(p0.toException())
                    override fun onChildMoved(p0: DataSnapshot, p1: String?) = Unit
                    override fun onChildChanged(p0: DataSnapshot, p1: String?) = Unit
                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        val conversationList: List<String> = p0.children.map { p0Children ->
                            p0Children.key.toString()
                        }
                        emitter.onNext(conversationList)
                    }

                    override fun onChildRemoved(p0: DataSnapshot) = Unit
                })
        }
    }

    override fun getlastMessageOfConversation(
        conversationId: String,
        onEmptyChat: () -> Unit?
    ): Observable<Message> {
        return Observable.create { emitter ->
            mFirebaseDatabaseReference
                .child(CONVERSATIONS_PATH)
                .child(CHILD_LAST_MESSAGE)
                .orderByKey()
                .equalTo(conversationId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            p0.children.map { p0Child ->
                                val lastMessage = p0Child.getValue(Message::class.java)
                                if (lastMessage != null) {
                                    emitter.onNext(lastMessage)
                                }
                            }
                        } else onEmptyChat()
                    }
                })
        }
    }

//    override fun getlastMessageOfConversation(
//        conversationId: String,
//        onEmptyChat: () -> Unit?
//    ): Observable<Message> {
//        return Observable.create { emitter ->
//            mFirebaseDatabaseReference
//                .child(MESSAGES_PATH)
//                .child(conversationId)
//                .limitToLast(1)
//                .addListenerForSingleValueEvent(object : ValueEventListener {
//                    override fun onCancelled(p0: DatabaseError) = emitter.onError(p0.toException())
//                    override fun onDataChange(p0: DataSnapshot) {
//                        if (p0.exists()) {
//                            p0.children.map { message ->
//                                val lastMessage = message.getValue(Message::class.java)
//                                if (lastMessage != null) {
//                                    emitter.onNext(lastMessage)
//                                }
//                            }
//                        } else onEmptyChat()
//                    }
//                })
//        }
//    }

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
                    if (user != null) {
                        val friend = Contact().apply {
                            id = user.id
                            conversationId = contactKey
                            name = user.name
                            email = user.email
                            imageUri = user.imageUri
                        }
                        mFirebaseDatabaseReference.child(CHILD_CONTACT_PATH)
                            .child(FirebaseAuthImpl.getUserId())
                            .child(contactKey)
                            .setValue(friend)
                            .addOnCompleteListener { task ->
                                if (task.isComplete && task.isSuccessful) {
                                    //friend add current user
                                    val currentUser = Contact().apply {
                                        id = FirebaseAuthImpl.getUserId()
                                        name = FirebaseAuthImpl.getUsername()
                                        email = FirebaseAuthImpl.getUserEmail()
                                        conversationId = contactKey
                                        imageUri = FirebaseAuthImpl.getUserImageUri()
                                    }
                                    mFirebaseDatabaseReference
                                        .child(CHILD_CONTACT_PATH)
                                        .child(user.id.toString())
                                        .child(contactKey)
                                        .setValue(currentUser)
                                        .addOnCompleteListener { task2 ->
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
                    } else
                        emitter.onError(Throwable("$friendEmail $DOES_NOT_EXISTS_ERROR"))
                }
        }
    }
}
