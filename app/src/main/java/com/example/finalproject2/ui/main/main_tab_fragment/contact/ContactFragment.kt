package com.example.finalproject2.ui.main.main_tab_fragment.contact

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject2.R
import com.example.finalproject2.model.User
import com.example.finalproject2.ultis.*
import kotlinx.android.synthetic.main.fragment_contact.*

class ContactFragment : Fragment(), ContactContract.View, OnRecyclerViewItemClickListener {

    private val mPresenter by lazy { ContactPresenter() }
    private var mListener: OnFragmentInteractionListener? = null
    private var mNavigator: ContactNavigator? = null
    lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)
        mNavigator = ContactNavigator(view.context)
        mContext = view.context
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        addTextChangeListener()
        addFriendButton.setOnClickListener {
            onAddFriendButtonClick()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onStop()
    }

    override fun onAddFriendButtonClick() {
        setVisibilityWhenAddingFriend(true)
        mPresenter.addFriend()
    }

    override fun onAddFriendSuccess() {
        setVisibilityWhenAddingFriend(false)
        activity?.applicationContext?.toast(getString(R.string.onSuccess))
        searchFriendEditText.text?.clear()
    }

    override fun onAddFriendError(exception: String?) {
        setVisibilityWhenAddingFriend(false)
        activity?.applicationContext?.toast("$exception")
    }

    override fun onGetContactSuccess(friendList: ArrayList<User>) {
        loadingContactListProgressBar.gone()
        contactRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = ContactRecyclerViewAdapter(
                friendList,
                activity?.applicationContext, this@ContactFragment
            )
            visible()
        }
    }

    override fun onGetContactError(error: String) {
        activity?.applicationContext?.toast(error)
    }

    override fun onItemClick(user: User) {
        mPresenter.accessConversation(user)
        mListener?.onFragmentInteraction(user)
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(user: User)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    private fun init() {
        loadingContactListProgressBar.visible()
        mPresenter.setView(this)
        mPresenter.getContactList(mPresenter.getCurrentUserId())
    }

    private fun setVisibilityWhenAddingFriend(isProcessing: Boolean) {
        if (isProcessing) {
            addingFriendProgressBar.visible()
            addFriendButton.gone()
        } else {
            addingFriendProgressBar.gone()
            addFriendButton.visible()
        }
    }

    private fun addTextChangeListener() {
        searchFriendEditText.onTextChanged { email ->
            addFriendButton.isEnabled = email != mPresenter.getCurrentUserEmail()
            if (!ValidationCheck.isEmailValid(email)) {
                searchFriendEditText.setError(getString(R.string.emailInputError), null)
                addFriendButton.disable()
            }
            mPresenter.onSearchInputChange(email)
        }
    }
}
