package com.example.finalproject2.ui.main

import com.example.finalproject2.firebase.database.FirebaseDatabaseImp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainPresenter : MainContract.Presenter {

    private lateinit var mView: MainContract.View
    private var mCompositeDisposable = CompositeDisposable()

    override fun addFriend(friendEmail: String) {
        val disposable =
            FirebaseDatabaseImp
                .addFriend(friendEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mView.onAddFriendSuccess()
                }, { t: Throwable? ->
                    val error = t?.message.toString()
                    mView.onAddFriendError(error)
                })
        mCompositeDisposable.add(disposable)
    }
    override fun setView(view: MainContract.View) {
        this.mView = view
    }
    override fun onStart() = Unit
    override fun onStop() = mCompositeDisposable.clear()
}
