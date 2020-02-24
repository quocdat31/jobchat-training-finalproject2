package com.example.finalproject2.ui.main.main_tab_fragment.personal

import com.example.finalproject2.firebase.authentication.FirebaseAuthImpl

class PersonalPresenter :
    PersonalContract.Presenter {

    private lateinit var mView: PersonalContract.View

    override fun logOut() {
        FirebaseAuthImpl.logout()
    }

    override fun setView(view: PersonalContract.View) {
        this.mView = view
    }

    override fun onStart() = Unit

    override fun onStop() = Unit
}
