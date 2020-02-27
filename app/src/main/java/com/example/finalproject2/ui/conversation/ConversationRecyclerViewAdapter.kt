//package com.example.finalproject2.ui.conversation
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.finalproject2.R
//import com.example.finalproject2.model.Message
//import com.example.finalproject2.ultis.gone
//import com.example.finalproject2.ultis.visible
//import kotlinx.android.synthetic.main.received_message_item.view.*
//
//interface OnRecyclerViewItemClickListener {
//    fun onItemClick(message: Message)
//}
//
//class ConversationRecyclerViewAdapter(
//    private val items: ArrayList<Message>,
//    private val context: Context?,
//    private val onRecyclerViewItemClickListener: OnRecyclerViewItemClickListener
//) :
//    RecyclerView.Adapter<ConversationRecyclerViewAdapter.ViewHolder>() {
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val messageContent: TextView = itemView.receivedMessageItemContentTextView
//        val messageDate: TextView = itemView.receivedMessageItemDateTextView
//        fun bind(
//            message: Message,
//            onRecyclerViewItemClickListener: OnRecyclerViewItemClickListener
//        ) {
//            itemView.setOnClickListener {
//                if (messageDate.visibility == View.GONE)
//                    messageDate.visible()
//                else
//                    messageDate.gone()
//                onRecyclerViewItemClickListener.onItemClick(message)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(
//            LayoutInflater.from(context).inflate(
//                R.layout.received_message_item,
//                parent,
//                false
//            )
//        )
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.apply {
//            messageContent.text = items[position].text
//            messageDate.text = items[position].time
//        }
//
//        val message = items[position]
//
//        holder.bind(message, onRecyclerViewItemClickListener)
//    }
//}

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
import com.example.finalproject2.ultis.gone
import com.example.finalproject2.ultis.visible
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.received_message_item.view.*
import kotlinx.android.synthetic.main.sent_message_item.view.*

interface OnRecyclerViewItemClickListener {
    fun onItemClick(message: Message)
}

class ConversationRecyclerViewAdapter(
    private val items: ArrayList<Message>,
    private val context: Context?,
    private val onRecyclerViewItemClickListener: OnRecyclerViewItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val SENT_MESSAGE_VIEW_TYPE = 0
        const val RECEIVED_MESSAGE_VIEW_TYPE = 1
    }

    class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageContent: TextView = itemView.receivedMessageItemContentTextView
        val messageDate: TextView = itemView.receivedMessageItemDateTextView
        val messageAvatar: ImageView = itemView.receivedMessageItemAvatarImageView
        fun bind(
            message: Message,
            onRecyclerViewItemClickListener: OnRecyclerViewItemClickListener
        ) {
            itemView.setOnClickListener {
                if (messageDate.visibility == View.GONE)
                    messageDate.visible()
                else
                    messageDate.gone()
                onRecyclerViewItemClickListener.onItemClick(message)
            }
        }
    }

    class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageContent: TextView = itemView.sentMessageItemContentTextView
        val messageDate: TextView = itemView.sentMessageITemDateTextView
        fun bind(
            message: Message,
            onRecyclerViewItemClickListener: OnRecyclerViewItemClickListener
        ) {
            itemView.setOnClickListener {
                if (messageDate.visibility == View.GONE)
                    messageDate.visible()
                else
                    messageDate.gone()
                onRecyclerViewItemClickListener.onItemClick(message)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SENT_MESSAGE_VIEW_TYPE) {
            SentMessageViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.sent_message_item,
                    parent,
                    false
                )
            )
        } else ReceivedMessageViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.received_message_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position].sender == FirebaseAuth.getInstance().currentUser?.uid) SENT_MESSAGE_VIEW_TYPE else RECEIVED_MESSAGE_VIEW_TYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SentMessageViewHolder) {
            holder.apply {
                messageContent.text = items[position].text
                messageDate.text = items[position].time
                bind(items[position], onRecyclerViewItemClickListener)
            }
        }
        if (holder is ReceivedMessageViewHolder) {
            holder.apply {
                //if (items[position].time - items[position+1].time <= 60)
                //TODO: hide avatar if messages receive distance time <= 60s
                messageContent.text = items[position].text
                messageDate.text = items[position].time
                bind(items[position], onRecyclerViewItemClickListener)
            }
        }
    }

    fun updateMessage(message: Message) {
        items.add(message)
        notifyDataSetChanged()
    }
}
