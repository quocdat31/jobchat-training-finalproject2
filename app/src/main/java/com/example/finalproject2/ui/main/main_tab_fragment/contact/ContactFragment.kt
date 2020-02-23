package com.example.finalproject2.ui.main.main_tab_fragment.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.contactPresenter
import com.example.finalproject.ui.home.main_tab_fragment.contact.ContactRecyclerViewAdapter
import com.example.finalproject.ui.home.main_tab_fragment.contact.OnRecyclerViewItemClickListener
import com.example.finalproject2.R
import com.example.finalproject2.model.Friend
import com.example.finalproject2.ultis.ValidationCheck
import com.example.finalproject2.ultis.disable
import com.example.finalproject2.ultis.onTextChanged
import com.example.finalproject2.ultis.toast

import kotlinx.android.synthetic.main.fragment_contact.*

class ContactFragment : Fragment(), ContactContract.View, OnRecyclerViewItemClickListener {

    private val mPresenter by lazy { contactPresenter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
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

    override fun onSearchInputError() {

    }

    override fun onGetContactSuccess(friendList: ArrayList<Friend>) {
        loadingContactListProgressBar.visibility = View.GONE
        contactRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = ContactRecyclerViewAdapter(
                friendList,
                activity?.applicationContext,
                ContactFragment()
            )
            visibility = View.VISIBLE
        }
    }

    override fun onGetContactError(error: String) {
        activity?.applicationContext?.toast(error)
    }

    override fun onItemClick(friend: Friend) {
        //TODO: start chat
    }

    private fun init() {
        loadingContactListProgressBar.visibility = View.VISIBLE

        mPresenter.setView(this)
        mPresenter.getContactList(mPresenter.getCurrentUserId())

        searchFriendEditText.onTextChanged { email ->
            addFriendButton.isEnabled = email != mPresenter.getCurrentUserEmail()

            if (!ValidationCheck.isEmailValid(email)) {
                searchFriendEditText.setError(getString(R.string.emailInputError), null)
                addFriendButton.disable()
            }
            mPresenter.onSearchInputChange(email)
        }

        addFriendButton.setOnClickListener {
            onAddFriendButtonClick()
        }

    }

    private fun setVisibilityWhenAddingFriend(isProcessing: Boolean) {

        if (isProcessing) {
            addingFriendProgressBar.visibility = View.VISIBLE
            addFriendButton.visibility = View.GONE
        } else {
            addingFriendProgressBar.visibility = View.GONE
            addFriendButton.visibility = View.VISIBLE
        }

    }
}
