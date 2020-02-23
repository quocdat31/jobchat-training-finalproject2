package com.example.finalproject2.ui.signin

import android.util.Log
import com.example.finalproject2.firebase.authentication.FirebaseAuthInterface
import com.example.finalproject2.model.SignInRequest
import com.example.finalproject2.ultis.ValidationCheck
import javax.inject.Inject

class SignInPresenter @Inject constructor(private val firebaseAuthInterface: FirebaseAuthInterface) :
    SignInContract.Presenter {

    private lateinit var mView: SignInContract.View
    private var mLoginRequest = SignInRequest()


    override fun onSubmitLogin() {
        mView.showProgressBar()
        if (ValidationCheck.isSignInValid(mLoginRequest)) {
            firebaseAuthInterface.signIn(
                mLoginRequest.email.toString(),
                mLoginRequest.password.toString()
            )
        }
    }

    override fun onEmailChange(email: String) {
        if (!ValidationCheck.isEmailValid(email)) {
            mView.showEmailError()
        }
        mLoginRequest.email = email
    }

    override fun onPasswordChange(password: String) {
        if (!ValidationCheck.isPasswordValid(password)) {
            mView.showPasswordError()
        }
        mLoginRequest.password = password
    }

    override fun setView(view: SignInContract.View) {
        this.mView = view
        Log.d("asd", "setview")
    }

    override fun onStart() = Unit

    override fun onStop() = Unit

}
