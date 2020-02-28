package com.example.finalproject2.ui.signin

import android.content.Context
import com.example.finalproject2.base.BaseNavigator
import com.example.finalproject2.ui.main.MainActivity
import com.example.finalproject2.ui.signup.SignUpActivity

class SignInNavigator(context: Context) : BaseNavigator(context),
    SignInContract.Navigator {
    override fun navigateHomeScreen() {
        context.startActivity(MainActivity.getInstance(context))
    }

    override fun navigateSignUpScreen() {
        context.startActivity(SignUpActivity.getInstance(context))
    }
}
