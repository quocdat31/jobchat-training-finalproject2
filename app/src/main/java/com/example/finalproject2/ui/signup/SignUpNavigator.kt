package com.example.finalproject.ui.register

import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject2.base.BaseNavigator
import com.example.finalproject2.ui.main.MainActivity

class SignUpNavigator(activity: AppCompatActivity) : BaseNavigator(activity),
    SignUpContract.Navigator {
    override fun navigateMainScreen() {
        activity.startActivity(MainActivity.getInstance(activity))
    }
}
