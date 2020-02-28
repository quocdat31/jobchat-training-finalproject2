package com.example.finalproject2.ui.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject2.R
import com.example.finalproject2.ultis.extension.*
import kotlinx.android.synthetic.main.activity_signin.*


class SignInActivity : AppCompatActivity(), SignInContract.View {

    companion object {
        fun getInstance(context: Context): Intent {
            return Intent(context, SignInActivity::class.java)
        }
    }

    private val mPresenter by lazy { SignInPresenter() }
    private lateinit var mNavigator: SignInNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        initView()
        addEditTextListener()
        initSlideViewPager()

    }

    override fun showProgressBar() {
        signInButtonTextView.gone()
        signInButtonProgressBar.visible()
    }

    override fun showPasswordError() {
        signInPasswordEditText.setError(getString(R.string.passwordInputError), null)
    }

    override fun showEmailError() {
        signInEmailEditText.error = getString(R.string.emailInputError)
    }

    override fun showLoginError(error: String) {
        this.toast(error)
        if (signInButtonTextView.isGone()) {
            signInButtonTextView.visible()
            signInButtonProgressBar.gone()
        }
    }

    override fun onLoginSuccess() {
        signInButtonProgressBar.gone()
        signInSuccessImageView.visible()
        mNavigator.navigateHomeScreen()
    }

    override fun onSignUpButtonClick() {
        mNavigator.navigateSignUpScreen()
    }

    private fun initView() {
        mPresenter.setView(this)
        mNavigator = SignInNavigator(this)
    }

    private fun addEditTextListener() {
        signInEmailEditText.onTextChanged { email ->
            mPresenter.onEmailChange(email)
        }
        signInPasswordEditText.onTextChanged { password ->
            mPresenter.onPasswordChange(password)
        }
        signInButtonTextView.setOnClickListener {
            mPresenter.onSubmitLogin()
        }
        signUpButton.setOnClickListener {
            onSignUpButtonClick()
        }
    }

    private fun initSlideViewPager() {
        signInActivitySlideIndicator.setupWithViewPager(signInViewPager)
        signInViewPager.adapter = SlideAdapter(this)
    }

}
