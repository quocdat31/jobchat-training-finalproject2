package com.example.finalproject2.ui.signup

import android.content.Context
import android.net.Uri
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
        fun showSignUpError()
    }

    interface Presenter : BasePresenter<View> {
        fun onUsernameChange(username: String)
        fun onEmailChange(email: String)
        fun onPasswordChange(password: String)
        fun onConfirmPasswordChange(confirmPassword: String)
        fun onPickImage(imageUri: Uri)
        fun onSubmitRegister()
    }

    interface Navigator {
        fun navigateMainScreen()
    }
}
