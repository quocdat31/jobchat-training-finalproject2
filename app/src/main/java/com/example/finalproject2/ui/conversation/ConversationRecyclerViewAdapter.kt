package com.example.finalproject2.ui.conversation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject2.R
import com.example.finalproject2.model.Message
import com.example.finalproject2.model.User
import com.example.finalproject2.ultis.extension.gone
import com.example.finalproject2.ultis.extension.visible
import com.example.finalproject2.ultis.listener.ItemClickListener
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.contact_friend_item.view.*
import kotlinx.android.synthetic.main.received_message_item.view.*
import kotlinx.android.synthetic.main.sent_message_item.view.*
import kotlinx.android.synthetic.main.user_detail_cardview.view.*

class ConversationRecyclerViewAdapter(
    private val items: ArrayList<Message>,
    private val context: Context?,
    private val itemClickListener: ItemClickListener<Message>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mUser: User

    companion object {
        const val SENT_MESSAGE_VIEW_TYPE = 0
        const val RECEIVED_MESSAGE_VIEW_TYPE = 1
        const val HEADER_VIEW_TYPE = 2
    }

    class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageContent: TextView = itemView.receivedMessageItemContentTextView
        val messageDate: TextView = itemView.receivedMessageItemDateTextView
        val messageAvatar: ImageView = itemView.receivedMessageItemAvatarImageView
        fun bind(
            message: Message,
            itemClickListener: ItemClickListener<Message>
        ) {
            itemView.setOnClickListener {
                if (messageDate.visibility == View.GONE)
                    messageDate.visible()
                else
                    messageDate.gone()
                itemClickListener.onClick(message)
            }
        }
    }

    class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageContent: TextView = itemView.sentMessageItemContentTextView
        val messageDate: TextView = itemView.sentMessageITemDateTextView
        fun bind(
            message: Message,
            itemClickListener: ItemClickListener<Message>
        ) {
            itemView.setOnClickListener {
                if (messageDate.visibility == View.GONE)
                    messageDate.visible()
                else
                    messageDate.gone()
                itemClickListener.onClick(message)
            }
        }
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.userDetail.friendNameTextView
        val userEmail: TextView = itemView.userDetail.friendEmailTextView
        val userAvatar: ImageView = itemView.userDetail.friendAvatarImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SENT_MESSAGE_VIEW_TYPE -> {
                SentMessageViewHolder(
                    LayoutInflater
                        .from(context)
                        .inflate(R.layout.sent_message_item, parent, false)
                )
            }
            HEADER_VIEW_TYPE -> {
                HeaderViewHolder(
                    LayoutInflater
                        .from(context)
                        .inflate(R.layout.user_detail_cardview, parent, false)
                )

            }
            else -> ReceivedMessageViewHolder(
                LayoutInflater
                    .from(context)
                    .inflate(R.layout.received_message_item, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            items[position].sender == FirebaseAuth.getInstance().currentUser?.uid -> SENT_MESSAGE_VIEW_TYPE
            position == 0 -> HEADER_VIEW_TYPE
            else -> RECEIVED_MESSAGE_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SentMessageViewHolder) {
            holder.apply {
                messageContent.text = items[position].text
                messageDate.text = items[position].time
                bind(items[position], itemClickListener)
            }
        }
        if (holder is ReceivedMessageViewHolder) {
            Picasso.get()
                .load(mUser.imageUri)
                .into(holder.messageAvatar)
            holder.apply {
                //if (items[position].time - items[position+1].time <= 60)
                //TODO: hide avatar if messages receive distance time <= 60s
                messageContent.text = items[position].text
                messageDate.text = items[position].time
                bind(items[position], itemClickListener)
            }
        }
        if (holder is HeaderViewHolder) {
            Picasso.get()
                .load(mUser.imageUri)
                .into(holder.userAvatar)
            holder.apply {
                userEmail.text = mUser.email
                username.text = mUser.name
            }
        }
    }

    fun updateMessage(message: Message) {
        items.add(message)
        notifyItemInserted(items.size)
    }

    fun getUser(user: User) {
        this.mUser = user
    }

}
