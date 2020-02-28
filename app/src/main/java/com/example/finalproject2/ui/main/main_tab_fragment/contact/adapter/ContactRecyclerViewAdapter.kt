package com.example.finalproject2.ui.main.main_tab_fragment.contact.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject2.R
import com.example.finalproject2.model.Contact
import com.example.finalproject2.ultis.listener.ItemClickListener
import com.example.finalproject2.ultis.toUri
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.contact_friend_item.view.*

class ContactRecyclerViewAdapter(
    private val items: ArrayList<Contact>,
    private val context: Context?,
    private val itemClickListener: ItemClickListener<Contact>
) :
    RecyclerView.Adapter<ContactRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendName: TextView = itemView.friendNameTextView
        val friendStatus: TextView = itemView.friendEmailTextView
        val friendImage: ImageView = itemView.friendAvatarImageView
        private val itemLayout: LinearLayout = itemView.friendItemRootLayout

        fun bind(
            contact: Contact,
            itemClickListener: ItemClickListener<Contact>
        ) {
            itemLayout.setOnClickListener {
                itemClickListener.onClick(contact)
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

            if (items[position].imageUri != "null") {
                Picasso.get()
                    .load(items[position].imageUri?.toUri())
                    .into(holder.friendImage)
                friendImage.setPadding(0, 0, 0, 0)
            } else {
                friendImage.setPadding(16, 16, 16, 16)
                friendImage.setImageResource(R.drawable.ic_person_black_24dp)
            }
        }

        val contact = items[position]
        holder.bind(contact, itemClickListener)
    }
}
