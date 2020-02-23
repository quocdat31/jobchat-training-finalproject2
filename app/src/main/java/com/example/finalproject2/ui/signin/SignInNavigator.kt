package com.example.finalproject2.ui.signin

import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject2.ui.signup.SignUpActivity
import com.example.finalproject2.base.BaseNavigator
import com.example.finalproject2.ui.main.MainActivity

class SignInNavigator(activity: AppCompatActivity) : BaseNavigator(activity),
    SignInContract.Navigator {
    override fun navigateHomeScreen() {
        activity.startActivity(MainActivity.getInstance(activity))
    }

    override fun navigateSignUpScreen() {
        activity.startActivity(SignUpActivity.getInstance(activity))
    }

}
