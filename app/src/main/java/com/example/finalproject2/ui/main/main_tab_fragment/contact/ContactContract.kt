package com.example.finalproject2.ui.main.main_tab_fragment.contact

import com.example.finalproject2.base.BasePresenter
import com.example.finalproject2.base.BaseView
import com.example.finalproject2.model.Friend

interface ContactContract {
    interface View : BaseView {
        fun onAddFriendButtonClick()
        fun onAddFriendSuccess()
        fun onAddFriendError(exception: String?)
        fun onGetContactSuccess(friendList: ArrayList<Friend>)
        fun onGetContactError(error: String)
    }
    interface Presenter : BasePresenter<View> {
        fun addFriend()
        fun onSearchInputChange(email: String)
        fun getCurrentUserEmail(): String
        fun getCurrentUserId(): String
        fun getContactList(id: String)
    }
}
