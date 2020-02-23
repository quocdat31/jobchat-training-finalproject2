package com.example.finalproject2.ui.main

import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject2.base.BaseNavigator
import com.example.finalproject2.ui.signin.SignInActivity


class MainNavigator(activity: AppCompatActivity) : BaseNavigator(activity),
    MainContract.Navigator {
    override fun navigateLoginScreen() {
        activity.startActivity(SignInActivity.getInstance(activity))
    }
}
