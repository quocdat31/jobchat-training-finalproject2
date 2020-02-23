package com.example.finalproject2.ui.main

import com.example.finalproject2.base.BasePresenter
import com.example.finalproject2.base.BaseView

interface MainContract {

    interface View : BaseView

    interface Presenter : BasePresenter<View>

    interface Navigator {
        fun navigateLoginScreen()
    }

}
