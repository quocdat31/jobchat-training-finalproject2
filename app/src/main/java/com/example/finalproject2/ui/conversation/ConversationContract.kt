package com.example.finalproject2.ui.conversation

import com.example.finalproject2.base.BasePresenter
import com.example.finalproject2.base.BaseView
import com.example.finalproject2.model.Message

interface ConversationContract {
    interface View : BaseView {
        fun onSendMessageError(error: String)
        fun onCreateConversationSuccess(conversationId: String)
        fun onEmptyMessageInput()
        fun onGetMessageSuccess(message: Message)
        fun onGetMessageError(error: String)
        fun onEmptyMessageList()
    }

    interface Presenter : BasePresenter<View> {
        fun sendMessage(conversationId: String, message: Message)
        fun getMessageList(conversationId: String)
        fun isEmptyMessageList(conversationId: String)
    }
}
