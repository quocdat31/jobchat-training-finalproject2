package com.example.finalproject2.ui.signin

import com.example.finalproject2.firebase.authentication.FirebaseAuthImpl
import com.example.finalproject2.model.SignInRequest
import com.example.finalproject2.ultis.ValidationCheck
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SignInPresenter :
    SignInContract.Presenter {

    private lateinit var mView: SignInContract.View
    private var mLoginRequest = SignInRequest()
    private var mCompositeDisposable = CompositeDisposable()

    override fun onSubmitLogin() {
        if (ValidationCheck.isSignInValid(mLoginRequest)) {
            mView.showProgressBar()
            val disposable =
                FirebaseAuthImpl.signIn(
                    mLoginRequest.email.toString(),
                    mLoginRequest.password.toString()
                ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mView.onLoginSuccess()
                    }, {
                        mView.showLoginError(it.message.toString())
                    })
            mCompositeDisposable.add(disposable)
        } else mView.showLoginError("Invalid input")
    }

    override fun onEmailChange(email: String) {
        if (!ValidationCheck.isEmailValid(email)) {
            mView.showEmailError()
        } else
            mLoginRequest.email = email
    }

    override fun onPasswordChange(password: String) {
        if (!ValidationCheck.isPasswordValid(password)) {
            mView.showPasswordError()
        } else
            mLoginRequest.password = password
    }

    override fun setView(view: SignInContract.View) {
        this.mView = view
    }

    override fun onStart() = Unit

    override fun onStop() = mCompositeDisposable.clear()
}
