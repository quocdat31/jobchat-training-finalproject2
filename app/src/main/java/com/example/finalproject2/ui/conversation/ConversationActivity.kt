package com.example.finalproject2.ui.conversation

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject2.R
import com.example.finalproject2.firebase.authentication.FirebaseAuthImpl
import com.example.finalproject2.firebase.database.FirebaseDatabaseImp
import com.example.finalproject2.model.Contact
import com.example.finalproject2.model.Message
import com.example.finalproject2.model.User
import com.example.finalproject2.ultis.extension.clear
import com.example.finalproject2.ultis.extension.getString
import com.example.finalproject2.ultis.extension.gone
import com.example.finalproject2.ultis.extension.toast
import com.example.finalproject2.ultis.listener.ItemClickListener
import kotlinx.android.synthetic.main.activity_conversation.*
import kotlinx.android.synthetic.main.main_tool_bar.*

class ConversationActivity : AppCompatActivity(), ConversationContract.View,
    ItemClickListener<Message> {

    companion object {
        fun getInstance(context: Context): Intent {
            return Intent(context, ConversationActivity::class.java)
        }
    }

    private val mPresenter: ConversationPresenter = ConversationPresenter()
    private lateinit var mConversationID: String
    private val EXTRA_KEY = "contact"
    private val INPUT_EMPTY_ERROR = "Cannot send empty message"
    lateinit var mAdapter: ConversationRecyclerViewAdapter
    lateinit var mUser: User
    private var mMessageList: ArrayList<Message> = arrayListOf()
    private val VISIBLE = View.VISIBLE
    private val GONE = View.GONE

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        mUser = intent.getSerializableExtra(EXTRA_KEY) as Contact
        initView(mUser as Contact)
        initConversationRecyclerView()

        conversationActivitySendMessageButton.setOnClickListener {
            if (conversationActivityMessageTextInput.getString() == "") {
                onEmptyMessageInput()
            } else {
                val conversationId = (mUser as Contact).conversationId.toString()
                val message = Message().apply {
                    sender = FirebaseAuthImpl.getUserId()
                    receiver = mUser.id
                    text = conversationActivityMessageTextInput.getString()
                }
                mPresenter.sendMessage(conversationId, message)
                conversationActivityMessageTextInput.clear()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onStop()
        FirebaseDatabaseImp.setMessageListener(null)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar_menu, menu)
        return true
    }

    override fun onSendMessageError(error: String) {
        this.toast(error)
    }

    override fun onCreateConversationSuccess(conversationId: String) {
        mConversationID = conversationId
    }

    override fun onEmptyMessageInput() {
        this.toast(INPUT_EMPTY_ERROR)
    }

    override fun onGetMessageSuccess(message: Message) {
        mAdapter.updateMessage(message)
        conversationActivityRecyclerView.smoothScrollToPosition(mAdapter.itemCount - 1)
        if (conversationActivityProgressBar.visibility == VISIBLE)
            conversationActivityProgressBar.gone()
    }

    override fun onGetMessageError(error: String) {
        this.toast(error)
    }

    override fun onEmptyMessageList() {
        if (conversationActivityProgressBar.visibility == VISIBLE)
            conversationActivityProgressBar.gone()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configActionBar(contact: Contact) {
        setSupportActionBar(mainToolBar)
        supportActionBar?.apply {
            title = contact.name
            subtitle = contact.email
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initView(contact: Contact) {
        mPresenter.isEmptyMessageList(contact.conversationId.toString())
        mPresenter.getMessageList(contact.conversationId.toString())
        mPresenter.setView(this)
        configActionBar(contact)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initConversationRecyclerView() {
        mMessageList.add(Message())
        mAdapter = ConversationRecyclerViewAdapter(mMessageList, this, this)

        conversationActivityRecyclerView.apply {
            adapter = mAdapter
            layoutManager =
                LinearLayoutManager(this@ConversationActivity)
            if (!mUser.imageUri.equals("null")) {
                mAdapter.getUser(mUser)
            }
            if (mAdapter.itemCount != 0) {
                smoothScrollToPosition(mAdapter.itemCount - 1)
            }
        }
    }

    override fun onClick(model: Message) = Unit

    override fun onLongClick(model: Message) = Unit

//    private fun setUpUserDetail() {
//        conversationActivityUserDetailCardView.userDetail.apply {
//            friendEmailTextView.text = mUser.email
//            friendNameTextView.text = mUser.name
//        }
//        Picasso.get()
//            .load(mUser.imageUri)
//            .into(conversationActivityUserDetailCardView.userDetail.friendAvatarImageView)
//    }
}
