package com.example.finalproject2.ui.main.main_tab_fragment.contact

import com.example.finalproject2.firebase.authentication.FirebaseAuthImpl
import com.example.finalproject2.firebase.database.FirebaseDatabaseImp
import com.example.finalproject2.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ContactPresenter :
    ContactContract.Presenter {

    private lateinit var mView: ContactContract.View
    private var mFriend = User()
    private var mCompositeDisposable = CompositeDisposable()

    override fun onStart() = Unit

    override fun onStop() = mCompositeDisposable.clear()

    override fun addFriend() {
        val friendEmail = mFriend.email.toString()
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

    override fun onSearchInputChange(email: String) {
        mFriend.email = email
    }

    override fun getCurrentUserEmail(): String = FirebaseAuthImpl.getUserEmail()

    override fun getCurrentUserId(): String = FirebaseAuthImpl.getUserId()

    override fun getContactList(id: String) {
        val disposable =
            FirebaseDatabaseImp
                .getFriendList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: List<User>? ->
                    mView.onGetContactSuccess(t as ArrayList<User>)
                }, { t: Throwable? ->
                    mView.onAddFriendError(t?.message.toString())
                })
        mCompositeDisposable.add(disposable)
    }

    override fun setView(view: ContactContract.View) {
        this.mView = view
    }

    fun accessConversation(user: User) {
        val disposable =
            FirebaseDatabaseImp
                .accessConversation(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                }, {

                })
        mCompositeDisposable.add(disposable)
    }
}
