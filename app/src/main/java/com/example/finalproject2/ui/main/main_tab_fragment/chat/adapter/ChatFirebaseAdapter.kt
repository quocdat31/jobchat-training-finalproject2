package com.example.finalproject2.ui.main.main_tab_fragment.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject2.R
import com.example.finalproject2.firebase.FirebaseConstant
import com.example.finalproject2.firebase.authentication.FirebaseAuthImpl
import com.example.finalproject2.firebase.database.FirebaseDatabaseImp
import com.example.finalproject2.model.LastMessage
import com.example.finalproject2.model.User
import com.example.finalproject2.ultis.listener.AdapterListener
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

object ChatFirebaseAdapter {

    private val mFirebaseReference = FirebaseDatabase.getInstance().reference
    private val mCurrentUserUid = FirebaseAuthImpl.getUserId()
    private var mCompositeDisposable = CompositeDisposable()
    lateinit var mAdapterListener: AdapterListener

    private val query = mFirebaseReference
        .child(FirebaseConstant.Child.LAST_MESSAGE)

    val option = FirebaseRecyclerOptions
        .Builder<LastMessage>()
        .setQuery(query, LastMessage::class.java)
        .build()

    fun setAdapterListener(adapterListener: AdapterListener) {
        this.mAdapterListener = adapterListener
    }

    fun build(): FirebaseRecyclerAdapter<LastMessage, ChatViewHolder> {
        return object : FirebaseRecyclerAdapter<LastMessage, ChatViewHolder>(option) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.contact_friend_item, parent, false)
                return ChatViewHolder(
                    view
                )
            }

            override fun onBindViewHolder(p0: ChatViewHolder, p1: Int, p2: LastMessage) {

                val friendNameTextView = p0.username
                val friendStatusTextView = p0.lastMessage
                val friendAvatarImageView = p0.userAvatar
                friendStatusTextView.text = p2.text

                if (p2.receiver != mCurrentUserUid) {
                    val disposable =
                        FirebaseDatabaseImp
                            .findFriendById(p2.receiver.toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { t: User? ->
                                friendNameTextView.text = t?.name
                                Picasso.get()
                                    .load(t?.imageUri)
                                    .into(friendAvatarImageView)
                            }
                    mCompositeDisposable.add(disposable)
                } else {
                    val disposable =
                        FirebaseDatabaseImp
                            .findFriendById(p2.sender.toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { t: User? ->
                                friendNameTextView.text = t?.name
                                Picasso.get()
                                    .load(t?.imageUri)
                                    .into(friendAvatarImageView)
                            }
                    mCompositeDisposable.add(disposable)
                }
            }

            override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
                super.onDetachedFromRecyclerView(recyclerView)
                mCompositeDisposable.clear()
            }

            override fun onDataChanged() {
                super.onDataChanged()
                mAdapterListener.onDataChange()
            }
        }
    }
}
