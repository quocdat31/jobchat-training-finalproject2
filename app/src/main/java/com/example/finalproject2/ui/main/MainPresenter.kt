package com.example.finalproject2.ui.main

class MainPresenter : MainContract.Presenter {

    private lateinit var mView: MainContract.View

    override fun setView(view: MainContract.View) {
        this.mView = view
    }

    override fun onStart() = Unit

    override fun onStop() = Unit

}
