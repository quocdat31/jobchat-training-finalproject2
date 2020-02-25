package com.example.finalproject2.ui.main.main_tab_fragment.personal

import com.example.finalproject2.base.BasePresenter
import com.example.finalproject2.base.BaseView

interface PersonalContract {
    interface View: BaseView
    interface Presenter: BasePresenter<View> {
        fun logOut()
    }
}
