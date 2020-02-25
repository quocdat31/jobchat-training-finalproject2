package com.example.finalproject2.ui.main.main_tab_fragment.contact

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject2.contactPresenter
import com.example.finalproject.ui.home.main_tab_fragment.contact.ContactRecyclerViewAdapter
import com.example.finalproject.ui.home.main_tab_fragment.contact.OnRecyclerViewItemClickListener
import com.example.finalproject2.R
import com.example.finalproject2.model.Friend
import com.example.finalproject2.ultis.*
import kotlinx.android.synthetic.main.fragment_contact.*

class ContactFragment : Fragment(), ContactContract.View, OnRecyclerViewItemClickListener {

    private val mPresenter by lazy { contactPresenter() }
    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact, container, false)
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

    override fun onGetContactSuccess(friendList: ArrayList<Friend>) {
        loadingContactListProgressBar.gone()
        contactRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = ContactRecyclerViewAdapter(
                friendList,
                activity?.applicationContext,
                ContactFragment()
            )
            visible()
        }
    }

    override fun onGetContactError(error: String) {
        activity?.applicationContext?.toast(error)
    }

    override fun onItemClick(friend: Friend) {
        mListener?.onFragmentInteraction(friend)
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(friend: Friend)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
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
