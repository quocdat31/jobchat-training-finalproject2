package com.example.finalproject2.ui.main.main_tab_fragment.contact


import com.example.finalproject2.firebase.authentication.FirebaseAuthInterface
import com.example.finalproject2.firebase.database.FirebaseDatabaseInterface
import com.example.finalproject2.model.Friend
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ContactPresenter constructor(
    private val firebaseDatabaseInterface: FirebaseDatabaseInterface,
    private val firebaseAuthInterface: FirebaseAuthInterface
) :
    ContactContract.Presenter {

    private lateinit var mView: ContactContract.View
    private var mFriend = Friend()
    private var mCompositeDisposable = CompositeDisposable()

    override fun onStart() = Unit

    override fun onStop() = mCompositeDisposable.clear()

    override fun addFriend() {
        val disposable =
            firebaseDatabaseInterface.addFriend(
                firebaseAuthInterface.getUserId(),
                mFriend.email.toString()
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mView.onAddFriendSuccess()
                }, { t: Throwable? ->
                    mView.onAddFriendError(t?.message.toString())
                })
        mCompositeDisposable.add(disposable)
    }

    override fun onSearchInputChange(email: String) {
        mFriend.email = email
    }

    override fun getCurrentUserEmail(): String = firebaseAuthInterface.getUserEmail()

    override fun getCurrentUserId(): String = firebaseAuthInterface.getUserId()

    override fun getContactList(id: String) {
        val disposable =
            firebaseDatabaseInterface.getFriendList(getCurrentUserId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ t: List<Friend>? ->
                    mView.onGetContactSuccess(t as ArrayList<Friend>)
                }, { t: Throwable? ->
                    mView.onAddFriendError(t?.message.toString())
                })
        mCompositeDisposable.add(disposable)
    }

    override fun setView(view: ContactContract.View) {
        this.mView = view
    }
}
