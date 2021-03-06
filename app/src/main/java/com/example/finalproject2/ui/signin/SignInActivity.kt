package com.example.finalproject2.ui.signin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject2.R
import com.example.finalproject2.ultis.gone
import com.example.finalproject2.ultis.onTextChanged
import com.example.finalproject2.ultis.toast
import com.example.finalproject2.ultis.visible
import kotlinx.android.synthetic.main.activity_signin.*
import javax.inject.Inject

class SignInActivity : AppCompatActivity(), SignInContract.View {

    @Inject
    lateinit var mPresenter: SignInPresenter
    private lateinit var mNavigator: SignInNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        initView()
        addEditTextListener()

    }

    override fun showProgressBar() {
        submitLoginButton.gone()
        loginProgressBar.visible()
    }

    override fun showPasswordError() {
        signInPasswordEditText.setError(getString(R.string.passwordInputError), null)
    }

    override fun showEmailError() {
        signInEmailEditText.error = getString(R.string.emailInputError)
    }

    override fun showLoginError(error: String) {
        this.toast(error)
        if (submitLoginButton.visibility == View.GONE) {
            submitLoginButton.visible()
            loginProgressBar.gone()
        }
    }

    override fun onLoginSuccess() {
        loginProgressBar.gone()
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
        submitLoginButton.setOnClickListener {
            mPresenter.onSubmitLogin()
        }
        signUpButton.setOnClickListener {
            onSignUpButtonClick()
        }
    }
}
