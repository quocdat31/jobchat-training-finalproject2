package com.example.finalproject2.ui.main.main_tab_fragment.chat

import io.reactivex.disposables.CompositeDisposable

class ChatPresenter : ChatContract.Presenter {

    private lateinit var mView: ChatContract.View
    private var mCompositeDisposable = CompositeDisposable()

//    override fun getLastMessageListOfConversations() {
//        val onEmptyChat = { mView.onEmptyChat() }
//        val disposable =
//            FirebaseDatabaseImp.getConversation()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap { conversationId ->
//                    FirebaseDatabaseImp
//                        .getlastMessageOfConversation(conversationId, onEmptyChat)
//                }
//                .flatMap({ message ->
//                    FirebaseDatabaseImp
//                        .findFriendById(message.receiver.toString())
//                }, { message: Message, user: User ->
//                    Pair(message, user)
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ t: Pair<Message, User>? ->
//                    if (t != null)
//                        mView.onGetLastMessageListOfConversationsSuccess(t)
//                }, { t: Throwable? ->
//                    mView.onGetLastMessageListOfConversationError(t?.message.toString())
//                })
//        mCompositeDisposable.add(disposable)
//    }

    override fun setView(view: ChatContract.View) {
        this.mView = view
    }

    override fun onStart() = Unit
    override fun onStop() {
        mCompositeDisposable.clear()
    }
}
