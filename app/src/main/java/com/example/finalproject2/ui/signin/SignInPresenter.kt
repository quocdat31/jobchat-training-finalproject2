package com.example.finalproject2.ui.signin

import com.example.finalproject2.firebase.authentication.FirebaseAuthInterface
import com.example.finalproject2.model.SignInRequest
import com.example.finalproject2.ultis.ValidationCheck
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SignInPresenter @Inject constructor(private val firebaseAuthInterface: FirebaseAuthInterface) :
    SignInContract.Presenter {

    private lateinit var mView: SignInContract.View
    private var mLoginRequest = SignInRequest()
    private var mCompositeDisposable = CompositeDisposable()

    override fun onSubmitLogin() {
        mView.showProgressBar()
        if (ValidationCheck.isSignInValid(mLoginRequest)) {
            val disposable =
                firebaseAuthInterface.signIn(
                    mLoginRequest.email.toString(),
                    mLoginRequest.password.toString()
                ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mView.onLoginSuccess()
                    }, {
                        mView.showLoginError(it.message.toString())
                    })
            mCompositeDisposable.add(disposable)
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
    }

    override fun onStart() = Unit

    override fun onStop() = mCompositeDisposable.clear()
}
