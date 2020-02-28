package com.example.finalproject2.ui.main

import com.example.finalproject2.base.BasePresenter
import com.example.finalproject2.base.BaseView

interface MainContract {
    interface View : BaseView {
        fun onAddFriendSuccess()
        fun onAddFriendError(error: String)
    }

    interface Presenter : BasePresenter<View> {
        fun addFriend(friendEmail: String)
    }

    interface Navigator {
        fun navigateLoginScreen()
    }
}
