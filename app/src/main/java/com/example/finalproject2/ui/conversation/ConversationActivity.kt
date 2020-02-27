package com.example.finalproject2.ui.conversation

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject2.R
import com.example.finalproject2.firebase.authentication.FirebaseAuthImpl
import com.example.finalproject2.model.Message
import com.example.finalproject2.model.User
import com.example.finalproject2.ultis.clear
import com.example.finalproject2.ultis.getString
import com.example.finalproject2.ultis.gone
import com.example.finalproject2.ultis.toast
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
    private val INPUT_EMPTY_ERROR = "Cannot send empty message"
    lateinit var mAdapter: ConversationRecyclerViewAdapter
    lateinit var mUser: User

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        mUser = intent.getSerializableExtra(EXTRA_KEY) as User
        initView(mUser)

        conversationActivitySendMessageButton.setOnClickListener {
            if (conversationActivityMessageTextInput.getString() == "") {
                onEmptyMessageInput()
            } else {


                val message = Message().apply {
                    sender = FirebaseAuthImpl.getUserId()
                    receiver = mUser.id
                    text = conversationActivityMessageTextInput.getString()
                }
                mPresenter.sendMessage(mUser.id.toString(), message)


                conversationActivityMessageTextInput.clear()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onStop()
    }

    override fun onSendMessageSuccess(message: Message) {
        mAdapter.updateMessage(message)
        conversationActivityRecyclerView.smoothScrollToPosition(mAdapter.itemCount - 1)
    }

    override fun onSendMessageError(error: String) {
        this.toast(error)
    }

    override fun onGetMessageListSuccess(messageList: ArrayList<Message>) {
        mAdapter = ConversationRecyclerViewAdapter(messageList, this, this)
        conversationActivityRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@ConversationActivity)
            setHasFixedSize(true)
            if (messageList.isNotEmpty()) {
                smoothScrollToPosition(mAdapter.itemCount - 1)
            }
        }
        conversationActivityProgressBar.gone()
    }

    override fun onGetMessageListError(error: String) {
        this.toast(error)
    }

    override fun onCreateConversationSuccess(conversationId: String) {
        mConversationID = conversationId
    }

    override fun onEmptyMessageInput() {
        this.toast(INPUT_EMPTY_ERROR)
    }

    override fun onItemClick(message: Message) {
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
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

    private fun initView(user: User) {
        mPresenter.getMessageList(user.id.toString())
        mPresenter.setView(this)
        configActionBar(user)
    }
}
