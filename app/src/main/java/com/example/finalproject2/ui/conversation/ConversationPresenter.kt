package com.example.finalproject2.ui.conversation

import com.example.finalproject2.firebase.database.FirebaseDatabaseImp
import com.example.finalproject2.model.Message
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ConversationPresenter : ConversationContract.Presenter {

    private lateinit var mView: ConversationContract.View
    private var mCompositeDisposable = CompositeDisposable()

    override fun sendMessage(conversationId: String, message: Message) {
        val sendMessageDisposable =
            FirebaseDatabaseImp
                .sendMessage(conversationId, message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({

                }, { t: Throwable ->
                    mView.onSendMessageError(t.message.toString())
                })
        mCompositeDisposable.add(sendMessageDisposable)
    }

    override fun getMessageList(conversationId: String) {
        val onEmptyMessage = { mView.onEmptyMessageList() }
        val getMessageListDisposable =
            FirebaseDatabaseImp
                .getMessage(conversationId, onEmptyMessage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ message ->
                    mView.onGetMessageSuccess(message)
                }, { throwable ->
                    mView.onGetMessageError(throwable.message.toString())
                })
        mCompositeDisposable.add(getMessageListDisposable)
    }

    override fun isEmptyMessageList(conversationId: String) {
        val disposable =
            FirebaseDatabaseImp.isEmptyMessageList(conversationId)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ isEmpty ->
                    if (isEmpty)
                        mView.onEmptyMessageList()
                }, {
                })
        mCompositeDisposable.add(disposable)
    }

    override fun setView(view: ConversationContract.View) {
        this.mView = view
    }

    override fun onStart() = Unit
    override fun onStop() = mCompositeDisposable.clear()

}
