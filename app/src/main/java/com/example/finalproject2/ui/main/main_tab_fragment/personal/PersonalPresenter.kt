package com.example.finalproject2.ui.main.main_tab_fragment.personal

import com.example.finalproject2.firebase.authentication.FirebaseAuthInterface

class PersonalPresenter(private val firebaseAuthInterface: FirebaseAuthInterface) :
    PersonalContract.Presenter {

    private lateinit var mView: PersonalContract.View

    override fun logOut() {
        firebaseAuthInterface.logout()
    }

    override fun setView(view: PersonalContract.View) {
        this.mView = view
    }

    override fun onStart() = Unit

    override fun onStop() = Unit
}