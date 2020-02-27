package com.example.finalproject2.ui.conversation

import com.example.finalproject2.firebase.database.FirebaseDatabaseImp
import com.example.finalproject2.model.Message
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class ConversationPresenter : ConversationContract.Presenter {

    private lateinit var mView: ConversationContract.View
    private var mCompositeDisposable = CompositeDisposable()

    override fun sendMessage(conversationId: String, message: Message) {
        val sendMessageDisposable =
            FirebaseDatabaseImp
                .sendMessage(conversationId, message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    mView.onSendMessageSuccess(message)
                }, { t: Throwable ->
                    mView.onSendMessageError(t.message.toString())
                })
        mCompositeDisposable.add(sendMessageDisposable)
    }

    override fun getMessageList(conversationId: String) {
        val getMessageListDisposable =
            FirebaseDatabaseImp
                .getMessageList(conversationId).flatMapSingle { list ->
                    mView.onGetMessageListSuccess(list as ArrayList<Message>)
                    FirebaseDatabaseImp.startListeningMessageListChange(conversationId)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ message ->
                    mView.onSendMessageSuccess(message)
                }, { throwable ->
                    mView.onSendMessageError(throwable.message.toString())
                })
        mCompositeDisposable.add(getMessageListDisposable)
    }

    override fun setView(view: ConversationContract.View) {
        this.mView = view
    }

    override fun onStart() = Unit
    override fun onStop() = mCompositeDisposable.clear()

}
