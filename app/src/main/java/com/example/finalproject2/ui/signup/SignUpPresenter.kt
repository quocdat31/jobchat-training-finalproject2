package com.example.finalproject.ui.register

import com.example.finalproject2.firebase.authentication.FirebaseAuthInterface
import com.example.finalproject2.firebase.database.FirebaseDatabaseInterface
import com.example.finalproject2.model.SignUpRequest
import com.example.finalproject2.ultis.ValidationCheck
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class SignUpPresenter constructor(
    private val firebaseAuthInterface: FirebaseAuthInterface,
    private val firebaseDatabaseInterface: FirebaseDatabaseInterface
) : SignUpContract.Presenter {

    private lateinit var mView: SignUpContract.View
    private val mUser = SignUpRequest()

    private val mCompositeDisposable = CompositeDisposable()

    override fun onUsernameChange(username: String) {
        mUser.name = username
    }

    override fun onEmailChange(email: String) {
        if (!ValidationCheck.isEmailValid(email)) {
            mView.showEmailError()
        }
        mUser.email = email
    }

    override fun onPasswordChange(password: String) {
        if (!ValidationCheck.isPasswordValid(password)) {
            mView.showPasswordError()
        }
        mUser.password = password
    }

    override fun onConfirmPasswordChange(confirmPassword: String) {
        if (!ValidationCheck.isConfirmPasswordMatch(mUser.password.toString(), confirmPassword)) {
            mView.showPasswordMatchingError()
        }
        mUser.confirmPassword = confirmPassword
    }

    override fun onSubmitRegister() {
        if (ValidationCheck.isSignUpValid(mUser)) {
            mView.showProgressBar()
            val (name, email, password) = mUser
            signUp(email.toString(), password.toString(), name.toString())
        }
    }

    override fun setView(view: SignUpContract.View) {
        this.mView = view
    }

    override fun onStart() = Unit

    override fun onStop() = mCompositeDisposable.clear()

    private fun signUp(email: String, password: String, name: String) {
        val disposable = firebaseAuthInterface.signUp(email, password, name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mView.onRegisterSuccess()
                createUser(firebaseAuthInterface.getUserId(), name, email)
            }, {
                mView.onRegisterError(it.message.toString())
            })
        mCompositeDisposable.add(disposable)
    }

    private fun createUser(id: String, name: String, email: String) {
        val disposable = firebaseDatabaseInterface.createUser(id, name, email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
            }, {
                mView.onRegisterError(it.message.toString())
            })
        mCompositeDisposable.add(disposable)
    }
}
