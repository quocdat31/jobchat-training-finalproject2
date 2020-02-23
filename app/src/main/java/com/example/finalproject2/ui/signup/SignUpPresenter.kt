package com.example.finalproject2.ui.signup

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.finalproject2.firebase.database.FirebaseDatabaseInterface
import com.example.finalproject2.firebase.authentication.FirebaseAuthInterface
import com.example.finalproject2.model.SignUpRequest
import com.example.finalproject2.ultis.ValidationCheck
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class SignUpPresenter constructor(
    private val firebaseAuthInterface: FirebaseAuthInterface,
    private val firebaseDatabaseInterface: FirebaseDatabaseInterface
) : SignUpContract.Presenter {

    private lateinit var mView: SignUpContract.View
    private val mUser = SignUpRequest()

    private val REQUEST_CODE = 1
    private val TITLE = "Select image"
    private val IMAGE_PATH = "image/*"

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
            createUser(email.toString(), password.toString(), name.toString())

        }
    }

    override fun accessGallery(context: Context) {

        val intent = Intent().apply {
            type = IMAGE_PATH
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            context as Activity,
            Intent.createChooser(intent, TITLE),
            REQUEST_CODE,
            null
        )
    }

    override fun setView(view: SignUpContract.View) {
        this.mView = view
    }

    override fun onStart() = Unit

    override fun onStop() = Unit

    private fun signUp(email: String, password: String, name: String) {
        firebaseAuthInterface.signUp(email, password, name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Void> {
                override fun onComplete() = Unit
                override fun onSubscribe(d: Disposable) = Unit
                override fun onNext(t: Void) = Unit
                override fun onError(e: Throwable) = mView.onRegisterError(e.message.toString())
            })
    }

    private fun createUser(email: String, password: String, name: String) {
        firebaseDatabaseInterface.createUser(email, password, name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Void> {
                override fun onComplete() = mView.onRegisterSuccess()
                override fun onSubscribe(d: Disposable) = Unit
                override fun onNext(t: Void) = Unit
                override fun onError(e: Throwable) = mView.onRegisterError(e.message.toString())
            })
    }


}
