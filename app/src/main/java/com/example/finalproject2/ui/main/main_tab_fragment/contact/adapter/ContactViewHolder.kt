package com.example.finalproject2.ui.main.main_tab_fragment.contact.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contact_friend_item.view.*

class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val friendName: TextView = itemView.friendNameTextView
    val friendStatus: TextView = itemView.friendEmailTextView
    val friendImage: ImageView = itemView.friendAvatarImageView
}
