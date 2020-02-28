package com.example.finalproject2.ui.main.main_tab_fragment.chat.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contact_friend_item.view.*

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val username: TextView = itemView.friendNameTextView
    val lastMessage: TextView = itemView.friendEmailTextView
    val userAvatar: ImageView = itemView.friendAvatarImageView
}
