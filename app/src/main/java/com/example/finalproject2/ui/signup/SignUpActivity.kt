package com.example.finalproject2.ui.signup

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.finalproject.signUpPresenter
import com.example.finalproject.ui.register.SignUpNavigator
import com.example.finalproject2.R
import com.example.finalproject2.ultis.onTextChanged
import com.example.finalproject2.ultis.toast
import kotlinx.android.synthetic.main.activity_register.*
import java.io.IOException

class SignUpActivity : AppCompatActivity(), SignUpContract.View {

    companion object {
        fun getInstance(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }

    private val mPresenter by lazy { signUpPresenter() }
    private lateinit var mNavigator: SignUpNavigator
    private val REQUEST_CODE = 1

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initView()
        addEditTextListener()

    }

    override fun onRegisterSuccess() {
        this.toast(getString(R.string.onSuccess))
        mNavigator.navigateMainScreen()
        setVisibilityProgressBar(View.GONE)
    }

    override fun onRegisterError(e: String) {
        setVisibilityProgressBar(View.GONE)
        submitRegisterButton.visibility = View.VISIBLE
        this.toast(e)
    }

    override fun showPasswordError() {
        registerPasswordEditText.setError(getString(R.string.passwordInputError), null)
    }

    override fun showEmailError() {
        registerEmailEditText.error = getString(R.string.emailInputError)
    }

    override fun showPasswordMatchingError() {
        registerConfirmPasswordEditText.setError(getString(R.string.confirmPasswordInputError), null)
    }

    override fun showProgressBar() {
        setVisibilityProgressBar(View.VISIBLE)
        submitRegisterButton.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onAvatarImageViewClick() {
        if (ContextCompat.checkSelfPermission(
                this,
                READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) mPresenter.accessGallery(this) else ActivityCompat.requestPermissions(
            this, arrayOf(
                READ_EXTERNAL_STORAGE
            ), REQUEST_CODE
        )
    }

    private fun initView() {
        mPresenter.setView(this)
        mNavigator = SignUpNavigator(this)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun addEditTextListener() {
        submitRegisterButton.setOnClickListener {
            mPresenter.onSubmitRegister()
        }

        registerAvatarImageView.setOnClickListener {
            onAvatarImageViewClick()
        }

        registerEmailEditText.onTextChanged { email ->
            mPresenter.onEmailChange(email)
        }

        registerPasswordEditText.onTextChanged { password ->
            mPresenter.onPasswordChange(password)
        }

        registerConfirmPasswordEditText.onTextChanged { confirmPassword ->
            mPresenter.onConfirmPasswordChange(confirmPassword)
        }

        registerUsernameEditText.onTextChanged { username ->
            mPresenter.onUsernameChange(username)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val filePath = data?.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                registerAvatarImageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private fun setVisibilityProgressBar(visibility: Int){
        registerProgressBar.visibility = visibility
    }

}
