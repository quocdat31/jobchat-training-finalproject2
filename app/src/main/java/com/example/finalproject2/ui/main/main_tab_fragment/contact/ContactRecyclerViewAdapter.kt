package com.example.finalproject2.ui.main.main_tab_fragment.contact

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject2.R
import com.example.finalproject2.model.User
import kotlinx.android.synthetic.main.contact_friend_item.view.*

interface OnRecyclerViewItemClickListener {
    fun onItemClick(user: User)
}

class ContactRecyclerViewAdapter(
    private val items: ArrayList<User>,
    private val context: Context?,
    private val onRecyclerViewItemClickListener: OnRecyclerViewItemClickListener
) :
    RecyclerView.Adapter<ContactRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendName: TextView = itemView.friendNameTextView
        val friendStatus: TextView = itemView.friendEmailTextView
        val friendImage: AppCompatImageView = itemView.friendAvatarImageView
        private val itemLayout: LinearLayout = itemView.friendItemRootLayout

        fun bind(
            user: User,
            onRecyclerViewItemClickListener: OnRecyclerViewItemClickListener
        ) {
            itemLayout.setOnClickListener {
                onRecyclerViewItemClickListener.onItemClick(user)
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
            friendName.text = items[position].name
            friendStatus.text = items[position].email
            //TODO : allow upload image when sign up (or edit in profile) and display
        }

        val user = items[position]

        holder.bind(user, onRecyclerViewItemClickListener)
    }
}
