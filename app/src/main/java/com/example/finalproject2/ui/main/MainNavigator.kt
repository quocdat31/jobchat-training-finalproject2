package com.example.finalproject2.ui.main

import android.content.Context
import com.example.finalproject2.base.BaseNavigator
import com.example.finalproject2.ui.signin.SignInActivity


class MainNavigator(context: Context) : BaseNavigator(context),
    MainContract.Navigator {
    override fun navigateLoginScreen() {
        context.startActivity(SignInActivity.getInstance(context))
    }
}
