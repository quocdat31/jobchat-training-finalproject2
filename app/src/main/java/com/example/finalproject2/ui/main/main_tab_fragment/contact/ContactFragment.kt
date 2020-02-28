package com.example.finalproject2.ui.main.main_tab_fragment.contact

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject2.R
import com.example.finalproject2.model.Contact
import com.example.finalproject2.model.User
import com.example.finalproject2.ui.main.MainInteractionListener
import com.example.finalproject2.ui.main.main_tab_fragment.contact.adapter.ContactFirebaseAdapter
import com.example.finalproject2.ultis.extension.gone
import com.example.finalproject2.ultis.extension.toast
import com.example.finalproject2.ultis.extension.visible
import com.example.finalproject2.ultis.listener.AdapterListener
import com.example.finalproject2.ultis.listener.ItemClickListener
import kotlinx.android.synthetic.main.fragment_contact.*

class ContactFragment : Fragment(),
    ContactContract.View,
    ItemClickListener<Contact>,
    MainInteractionListener.ContactListener,
    AdapterListener {

    private val mPresenter by lazy { ContactPresenter() }
    private var mListener: OnFragmentInteractionListener? = null
    private var mNavigator: ContactNavigator? = null
    lateinit var mContext: Context
    lateinit var mAdapter: ContactFirebaseAdapter

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }

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
        initRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.onStop()
    }

    override fun onGetContactSuccess(contact: ArrayList<Contact>) {
        loadingContactListProgressBar.gone()
    }

    override fun onGetContactError(error: String) {
        activity?.applicationContext?.toast(error)
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
        mPresenter.onStart()
        mPresenter.setView(this)
    }

    private fun initRecyclerView() {
        mAdapter = ContactFirebaseAdapter()
        mAdapter.setAdapterListener(this)
        mAdapter.setItemClickListener(this)
        contactRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
        }
    }

    override fun onAddFriendSuccess(friendEmail: String) {

    }

    override fun onClick(model: Contact) {
        mListener?.onFragmentInteraction(model)
    }

    override fun onLongClick(model: Contact) = Unit

    override fun onDataChange() {
        loadingContactListProgressBar.gone()
    }

    override fun onError(error: String) {
        loadingContactListProgressBar.gone()
        mContext.toast(error)
    }
}
