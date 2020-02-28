package com.example.finalproject2.ui.main.main_tab_fragment.chat

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject2.R
import com.example.finalproject2.model.LastMessage
import com.example.finalproject2.model.Message
import com.example.finalproject2.model.User
import com.example.finalproject2.ui.main.main_tab_fragment.chat.adapter.ChatFirebaseAdapter
import com.example.finalproject2.ui.main.main_tab_fragment.chat.adapter.ChatViewHolder
import com.example.finalproject2.ultis.extension.gone
import com.example.finalproject2.ultis.listener.AdapterListener
import com.firebase.ui.database.FirebaseRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment(),
    ChatContract.View,
    AdapterListener {

    private val mPresenter by lazy { ChatPresenter() }
    lateinit var mAdapter: FirebaseRecyclerAdapter<LastMessage, ChatViewHolder>
    lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
        mPresenter.setView(this)
        mPresenter.onStart()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onStop()
    }

    private fun initRecyclerView() {
        mAdapter = ChatFirebaseAdapter.build()
        ChatFirebaseAdapter.setAdapterListener(this)

        chatFragmentRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
    }

    override fun onDataChange() {
        chatFragmentProgressBar.gone()
    }

    override fun onError(error: String) = Unit
}
