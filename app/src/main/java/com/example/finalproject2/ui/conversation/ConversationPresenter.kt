package com.example.finalproject2.ui.conversation

import android.util.Log
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
            FirebaseDatabaseImp.sendMessage(conversationId, message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    mView.onSendMessageSuccess()
                }, { t: Throwable ->
                    mView.onSendMessageError(t.message.toString())
                })
        mCompositeDisposable.add(sendMessageDisposable)
    }

    override fun createConversation(memberList: ArrayList<String>) {
        val createConversationDisposable =
            FirebaseDatabaseImp.createConversation(memberList).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ conversationId ->
                    getMessageList(conversationId)
                    mView.onCreateConversationSuccess(conversationId)
                }, { t: Throwable ->
                    mView.onSendMessageError(t.message.toString())
                })
        mCompositeDisposable.add(createConversationDisposable)
    }

    override fun getMessageList(conversationId: String) {
        val getMessageListDisposable =
            FirebaseDatabaseImp.getMessageList(conversationId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({ t: List<Message>? ->
                    mView.onGetMessageListSuccess(t as ArrayList<Message>)
                }, { t: Throwable? ->
                    mView.onGetMessageListError(t?.message.toString())
                })
        mCompositeDisposable.add(getMessageListDisposable)
    }

    override fun setView(view: ConversationContract.View) {
        this.mView = view
    }

    fun findConversation(friendId: String): String {
        var conversationId = ""
        val disposable = FirebaseDatabaseImp.findConversation(friendId)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t ->
                conversationId = t
                Log.d("asd", t.toString())
            }, {

            })
        mCompositeDisposable.add(disposable)
        return conversationId
    }

    override fun onStart() = Unit
    override fun onStop() = mCompositeDisposable.clear()

}
