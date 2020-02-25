package com.example.finalproject2.ui.main.main_tab_fragment.personal

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.finalproject2.personalPresenter
import com.example.finalproject2.R
import com.example.finalproject2.ui.main.MainNavigator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_personal.*

class PersonalFragment : Fragment() {

    private val mPresenter by lazy { personalPresenter() }
    lateinit var mNavigator: MainNavigator
    lateinit var mContext: Context

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mNavigator = MainNavigator(mContext)

        personalFragmentLogOutButton.setOnClickListener {
            mPresenter.logOut()

            if (FirebaseAuth.getInstance().currentUser == null) {
                mNavigator.navigateLoginScreen()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_personal, container, false)
    }
}
