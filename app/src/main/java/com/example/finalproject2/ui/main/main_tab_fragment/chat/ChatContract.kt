package com.example.finalproject2.ui.main.main_tab_fragment.chat

import com.example.finalproject2.base.BasePresenter
import com.example.finalproject2.base.BaseView

interface ChatContract {
    interface View : BaseView {
//        fun onGetLastMessageListOfConversationsSuccess(pair: Pair<Message, User>)
//        fun onGetLastMessageListOfConversationError(error: String)
//        fun onEmptyChat()
    }

    interface Presenter : BasePresenter<View> {
//        fun getLastMessageListOfConversations()
    }
}
