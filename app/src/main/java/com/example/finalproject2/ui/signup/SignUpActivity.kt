package com.example.finalproject2.ui.signup

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.finalproject2.R
import com.example.finalproject2.firebase.authentication.FirebaseAuthImpl
import com.example.finalproject2.ultis.extension.gone
import com.example.finalproject2.ultis.extension.onTextChanged
import com.example.finalproject2.ultis.extension.toast
import com.example.finalproject2.ultis.extension.visible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.sign_up_pick_image_options.*

class SignUpActivity : AppCompatActivity(), SignUpContract.View {

    companion object {
        fun getInstance(context: Context): Intent {
            return Intent(context, SignUpActivity::class.java)
        }
    }

    private val mPresenter by lazy { SignUpPresenter() }
    private lateinit var mNavigator: SignUpNavigator
    private val REQUEST_CODE = 1
    private val TITLE = "Select image"
    private val IMAGE_PATH = "image/*"
    lateinit var mImageUri: Uri
    lateinit var mBottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initView()
        addEditTextListener()
        mBottomSheetBehavior = BottomSheetBehavior.from(signUpPickImageOptionsBottomSheet)
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onStop()
    }

    override fun onRegisterSuccess() {
        this.toast(getString(R.string.onSuccess))
        mNavigator.navigateMainScreen()
        registerProgressBar.gone()
        FirebaseAuthImpl.signIn(
            registerEmailEditText.toString(),
            registerPasswordEditText.toString()
        )
    }

    override fun onRegisterError(e: String) {
        registerProgressBar.gone()
        submitRegisterButton.visible()
        this.toast(e)
    }

    override fun showPasswordError() {
        registerPasswordEditText.setError(getString(R.string.passwordInputError), null)
    }

    override fun showEmailError() {
        registerEmailEditText.error = getString(R.string.emailInputError)
    }

    override fun showPasswordMatchingError() {
        registerConfirmPasswordEditText.setError(
            getString(R.string.confirmPasswordInputError),
            null
        )
    }

    override fun showProgressBar() {
        registerProgressBar.visible()
        submitRegisterButton.gone()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onAvatarImageViewClick() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        signUpPickImageGallery.setOnClickListener {
            if (checkSelfPermission()) accessGallery() else requestPermission()
        }
    }

    override fun showSignUpError() {
        this.toast("Invalid input")
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
            mImageUri = data?.data!!
            Picasso.get().load(mImageUri).into(registerAvatarImageView)
            mPresenter.onPickImage(mImageUri)
        }
    }

    private fun getExtension(uri: Uri): String {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)).toString()
    }

    private fun checkSelfPermission(): Boolean = (ContextCompat.checkSelfPermission(
        this,
        READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED)

    private fun requestPermission() =
        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE), REQUEST_CODE)

    private fun accessGallery() {
        val intent = Intent().apply {
            type = IMAGE_PATH
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(intent, TITLE),
            REQUEST_CODE
        )
    }

    fun initBottomSheet() {
    }
}
