package com.example.finalproject2.ui.main.main_tab_fragment.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject2.R
import com.example.finalproject2.model.Message
import com.example.finalproject2.model.User
import com.squareup.picasso.Picasso

class ChatRecyclerViewAdapter(val context: Context, val arrayList: ArrayList<Pair<Message, User>>) :
    RecyclerView.Adapter<ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater
                .from(context)
                .inflate(R.layout.contact_friend_item, parent, false)
        )
    }

    override fun getItemCount(): Int = arrayList.size
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.apply {
            username.text = arrayList[position].second.name
            lastMessage.text = arrayList[position].first.text
        }
        Picasso.get()
            .load(arrayList[position].second.imageUri)
            .into(holder.userAvatar)
    }

    fun updateConversationList(pair: Pair<Message, User>) {
        arrayList.add(pair)
        notifyItemInserted(arrayList.size)
    }
}
