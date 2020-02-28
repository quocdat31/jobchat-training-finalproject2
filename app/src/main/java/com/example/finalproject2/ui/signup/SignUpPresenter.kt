package com.example.finalproject2.ui.signup

import android.net.Uri
import com.example.finalproject2.firebase.authentication.FirebaseAuthImpl
import com.example.finalproject2.firebase.database.FirebaseDatabaseImp
import com.example.finalproject2.model.SignUpRequest
import com.example.finalproject2.ultis.ValidationCheck
import com.example.finalproject2.ultis.toUri
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class SignUpPresenter : SignUpContract.Presenter {

    private lateinit var mView: SignUpContract.View
    private val mUser = SignUpRequest()
    private var mCompositeDisposable = CompositeDisposable()

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

    override fun onPickImage(imageUri: Uri) {
        mUser.imageUri = imageUri
    }

    override fun onSubmitRegister() {
        if (ValidationCheck.isSignUpValid(mUser)) {
            mView.showProgressBar()
            val (name, email, password, imageUri) = mUser
            signUp(
                email.toString(),
                password.toString(),
                name.toString(),
                imageUri.toString().toUri()
            )
        } else
            mView.showSignUpError()
    }

    override fun setView(view: SignUpContract.View) {
        this.mView = view
    }

    override fun onStart() = Unit
    override fun onStop() = mCompositeDisposable.clear()

    private fun signUp(email: String, password: String, name: String, imageUri: Uri) {
        val disposable =
            FirebaseDatabaseImp
                .uploadImage(imageUri)
                .flatMapCompletable { imageString ->
                    FirebaseAuthImpl.signUp(
                        email,
                        password,
                        name,
                        imageString.toUri()
                    )
                        .flatMapCompletable { userId ->
                            FirebaseDatabaseImp
                                .createUser(
                                    userId,
                                    name,
                                    email,
                                    imageString
                                )
                        }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mView.onRegisterSuccess()
                }, { t: Throwable? ->
                    mView.onRegisterError(t?.message.toString())
                })
        mCompositeDisposable.add(disposable)
    }

    private fun createUser(email: String, password: String, name: String, imageUri: String) {
        val disposable =
            FirebaseDatabaseImp.createUser(email, password, name, imageUri)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mView.onRegisterSuccess()
                }, { t: Throwable? ->
                    mView.onRegisterError(t?.message.toString())
                })
        mCompositeDisposable.add(disposable)
    }


}
