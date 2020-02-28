package com.example.finalproject2.ui.main.main_tab_fragment.setting

import com.example.finalproject2.base.BasePresenter
import com.example.finalproject2.base.BaseView

interface SettingContract {
    interface View: BaseView
    interface Presenter: BasePresenter<View> {
        fun logOut()
    }
}
