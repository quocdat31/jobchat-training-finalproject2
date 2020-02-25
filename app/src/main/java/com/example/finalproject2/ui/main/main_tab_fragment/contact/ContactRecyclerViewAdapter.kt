package com.example.finalproject.ui.home.main_tab_fragment.contact

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject2.R
import com.example.finalproject2.model.Friend
import kotlinx.android.synthetic.main.contact_friend_item.view.*

interface OnRecyclerViewItemClickListener {
    fun onItemClick(friend: Friend)
}

class ContactRecyclerViewAdapter(
    private val items: ArrayList<Friend>,
    private val context: Context?,
    private val onRecyclerViewItemClickListener: OnRecyclerViewItemClickListener
) :
    RecyclerView.Adapter<ContactRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendName: TextView = itemView.friendNameTextView
        val friendStatus: TextView = itemView.friendStatusTextView
        val friendImage: AppCompatImageView = itemView.friendAvatarImageView
        private val itemLayout: LinearLayout = itemView.friendItemRootLayout

        fun bind(
            friend: Friend,
            onRecyclerViewItemClickListener: OnRecyclerViewItemClickListener
        ) {
            itemLayout.setOnClickListener {
                onRecyclerViewItemClickListener.onItemClick(friend)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.contact_friend_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            friendStatus.text = items[position].status
            friendName.text = items[position].name
            //TODO : allow upload image when sign up (or edit in profile) and display
        }
        val gitUser = items[position]
        holder.bind(gitUser, onRecyclerViewItemClickListener)
    }
}
