package com.example.finalproject2.ui.conversation

import com.example.finalproject2.base.BasePresenter
import com.example.finalproject2.base.BaseView
import com.example.finalproject2.model.Message

interface ConversationContract {
    interface View : BaseView {
        fun onSendMessageSuccess(message: Message)
        fun onSendMessageError(error: String)
        fun onGetMessageListSuccess(messageList: ArrayList<Message>)
        fun onGetMessageListError(error: String)
        fun onCreateConversationSuccess(conversationId: String)
        fun onEmptyMessageInput()
    }

    interface Presenter : BasePresenter<View> {
        fun sendMessage(conversationId: String, message: Message)
        fun getMessageList(conversationId: String)
    }
}