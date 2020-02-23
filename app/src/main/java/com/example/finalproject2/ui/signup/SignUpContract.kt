package com.example.finalproject.ui.register

import android.content.Context
import com.example.finalproject2.base.BasePresenter
import com.example.finalproject2.base.BaseView

interface SignUpContract {
    interface View : BaseView {
        fun onRegisterSuccess()
        fun onRegisterError(e: String)
        fun showPasswordError()
        fun showEmailError()
        fun showPasswordMatchingError()
        fun showProgressBar()
        fun onAvatarImageViewClick()
    }

    interface Presenter : BasePresenter<View> {
        fun onUsernameChange(username: String)
        fun onEmailChange(email: String)
        fun onPasswordChange(password: String)
        fun onConfirmPasswordChange(confirmPassword: String)
        fun onSubmitRegister()
        fun accessGallery(context: Context)
    }

    interface Navigator {
        fun navigateMainScreen()
    }
}
