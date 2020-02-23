package com.example.finalproject2.ui.main.main_tab_fragment.contact


import com.example.finalproject2.firebase.authentication.FirebaseAuthInterface
import com.example.finalproject2.firebase.database.FirebaseDatabaseInterface
import com.example.finalproject2.model.Friend
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ContactPresenter constructor(
    private val firebaseDatabaseInterface: FirebaseDatabaseInterface,
    private val firebaseAuthInterface: FirebaseAuthInterface
) :
    ContactContract.Presenter {

    private lateinit var mView: ContactContract.View
    private var mFriend = Friend()

    override fun addFriend() {
        firebaseDatabaseInterface.addFriend(
            firebaseAuthInterface.getUserId(),
            mFriend.email.toString()
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Void> {
                override fun onComplete() = mView.onAddFriendSuccess()
                override fun onSubscribe(d: Disposable) = Unit
                override fun onNext(t: Void) = Unit
                override fun onError(e: Throwable) = mView.onAddFriendError(e.message)
            })
    }

    override fun onSearchInputChange(email: String) {
        mFriend.email = email
    }

    override fun getCurrentUserEmail(): String = firebaseAuthInterface.getUserEmail()

    override fun getCurrentUserId(): String = firebaseAuthInterface.getUserId()

    override fun getContactList(id: String) {
        firebaseDatabaseInterface.getFriendList(getCurrentUserId()).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<List<Friend>> {
                override fun onComplete() = Unit
                override fun onSubscribe(d: Disposable) = Unit
                override fun onNext(t: List<Friend>) =
                    mView.onGetContactSuccess(t as ArrayList<Friend>)
                override fun onError(e: Throwable) = mView.onGetContactError(e.message.toString())
            })
    }

    override fun setView(view: ContactContract.View) {
        this.mView = view
    }

    override fun onStart() = Unit

    override fun onStop() = Unit
}
