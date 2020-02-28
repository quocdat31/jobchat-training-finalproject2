package com.example.finalproject2.ui.main.main_tab_fragment.contact

import com.example.finalproject2.base.BasePresenter
import com.example.finalproject2.base.BaseView
import com.example.finalproject2.model.Contact
import com.example.finalproject2.model.User

interface ContactContract {
    interface View : BaseView {
        fun onGetContactSuccess(contact: ArrayList<Contact>)
        fun onGetContactError(error: String)
    }
    interface Presenter : BasePresenter<View> {
        fun onSearchInputChange(email: String)
        fun getCurrentUserEmail(): String
        fun getCurrentUserId(): String
        fun getContactList(id: String)
    }
    interface Navigator {
        fun navigateChatActivity()
    }
}
