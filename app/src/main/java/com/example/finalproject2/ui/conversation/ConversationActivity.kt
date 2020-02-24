package com.example.finalproject2.ui.conversation

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject2.R
import com.example.finalproject2.model.Message
import com.example.finalproject2.model.User
import com.example.finalproject2.ultis.clear
import com.example.finalproject2.ultis.getString
import com.example.finalproject2.ultis.toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_conversation.*
import java.util.*

class ConversationActivity : AppCompatActivity(), ConversationContract.View,
    OnRecyclerViewItemClickListener {

    companion object {
        fun getInstance(context: Context): Intent {
            return Intent(context, ConversationActivity::class.java)
        }
    }

    private val mPresenter: ConversationPresenter = ConversationPresenter()
    lateinit var mConversationID: String
    private val EXTRA_KEY = "user"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        val user: User = intent.getSerializableExtra(EXTRA_KEY) as User
        configActionBar(user)

        val memberList = arrayListOf<String>()
        memberList.add(user.id.toString())
        memberList.add(FirebaseAuth.getInstance().currentUser?.uid.toString())

        mPresenter.setView(this)

        if (mPresenter.findConversation(user.id.toString()).isEmpty())
            mPresenter.createConversation(memberList)

        conversationActivitySendMessageButton.setOnClickListener {
            val message = Message().apply {
                sender = FirebaseAuth.getInstance().currentUser?.uid.toString()
                receiver = user.id
                text = conversationActivityMessageTextInput.getString()
            }
            mPresenter.sendMessage(
                mConversationID,
                message
            )
            conversationActivityMessageTextInput.clear()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onStop()
    }

    private fun configActionBar(user: User) {
        setSupportActionBar(conversationActivityToolbar)
        supportActionBar?.apply {
            title = user.name
            subtitle = user.email
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSendMessageSuccess() {
        //TODO: notifyDataSetChange, scroll to bottom of recycler view
    }

    override fun onSendMessageError(error: String) {
        this.toast(error)
    }

    override fun onGetMessageListSuccess(messageList: ArrayList<Message>) {
        conversationActivityRecyclerView.adapter =
            ConversationRecyclerViewAdapter(messageList, this, this)
        conversationActivityRecyclerView.layoutManager = LinearLayoutManager(this)
        conversationActivityRecyclerView.setHasFixedSize(true)
    }

    override fun onGetMessageListError(error: String) {
        this.toast(error)
    }

    override fun onCreateConversationSuccess(conversationId: String) {
        mConversationID = conversationId
    }

    override fun onItemClick(message: Message) {

    }

}
