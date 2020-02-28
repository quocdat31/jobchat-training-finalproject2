package com.example.finalproject2.ui.main.main_tab_fragment.contact.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.finalproject2.R
import com.example.finalproject2.firebase.authentication.FirebaseAuthImpl
import com.example.finalproject2.firebase.database.FirebaseDatabaseImp
import com.example.finalproject2.model.Contact
import com.example.finalproject2.ultis.listener.AdapterListener
import com.example.finalproject2.ultis.listener.ItemClickListener
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

val query = FirebaseDatabase
    .getInstance()
    .reference
    .child("contacts")
    .child(FirebaseAuthImpl.getUserId())

val option =
    FirebaseRecyclerOptions
        .Builder<Contact>()
        .setQuery(query, Contact::class.java)
        .build()

class ContactFirebaseAdapter :
    FirebaseRecyclerAdapter<Contact, ContactViewHolder>(option) {

    private lateinit var itemClickListener: ItemClickListener<Contact>
    private lateinit var adapterListener: AdapterListener

    fun setItemClickListener(itemClickListener: ItemClickListener<Contact>) {
        this.itemClickListener = itemClickListener
    }

    fun setAdapterListener(adapterListener: AdapterListener) {
        this.adapterListener = adapterListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_friend_item, parent, false)
        return ContactViewHolder(
            view
        )
    }

    override fun onError(error: DatabaseError) {
        super.onError(error)
        adapterListener.onError(error.message)
    }

    override fun onDataChanged() {
        super.onDataChanged()
        adapterListener.onDataChange()
    }

    override fun onBindViewHolder(p0: ContactViewHolder, p1: Int, p2: Contact) {

        val friendNameTextView = p0.friendName
        val friendStatusTextView = p0.friendStatus
        val friendAvatarImageView = p0.friendImage
        val friendAvatarImageUri = p2.imageUri

        val disposable =
            FirebaseDatabaseImp
                .findFriendById(p2.id.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { user ->
                    friendStatusTextView.text = user.connectivityState
                }
        friendNameTextView.text = p2.name
        Picasso.get()
            .load(friendAvatarImageUri)
            .into(friendAvatarImageView)

        p0.itemView.setOnClickListener {
            itemClickListener.onClick(p2)
        }
    }
}
