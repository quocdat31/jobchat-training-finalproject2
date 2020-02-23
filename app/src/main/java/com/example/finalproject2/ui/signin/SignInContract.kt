package com.example.finalproject2.ui.signin

import com.example.finalproject2.base.BasePresenter
import com.example.finalproject2.base.BaseView

interface SignInContract {
    interface View : BaseView {
        fun showProgressBar()
        fun showPasswordError()
        fun showEmailError()
        fun showLoginError(error: String)
        fun onLoginSuccess()
        fun onSignUpButtonClick()
    }

    interface Presenter : BasePresenter<View> {
        fun onSubmitLogin()
        fun onEmailChange(email: String)
        fun onPasswordChange(password: String)
    }

    interface Navigator {
        fun navigateHomeScreen()
        fun navigateSignUpScreen()
    }
}
