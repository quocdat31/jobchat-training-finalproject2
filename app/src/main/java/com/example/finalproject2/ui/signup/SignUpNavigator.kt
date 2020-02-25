package com.example.finalproject2.ui.signup

import android.content.Context
import com.example.finalproject2.base.BaseNavigator
import com.example.finalproject2.ui.main.MainActivity

class SignUpNavigator(context: Context) : BaseNavigator(context),
    SignUpContract.Navigator {
    override fun navigateMainScreen() {
        context.startActivity(MainActivity.getInstance(context))
    }
}
