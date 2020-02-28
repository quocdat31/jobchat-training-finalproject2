package com.example.finalproject2.ui.main.main_tab_fragment.setting

import com.example.finalproject2.firebase.authentication.FirebaseAuthImpl

class SettingPresenter :
    SettingContract.Presenter {

    private lateinit var mView: SettingContract.View

    override fun logOut() {
        FirebaseAuthImpl.logout()
    }

    override fun setView(view: SettingContract.View) {
        this.mView = view
    }

    override fun onStart() = Unit

    override fun onStop() = Unit
}
