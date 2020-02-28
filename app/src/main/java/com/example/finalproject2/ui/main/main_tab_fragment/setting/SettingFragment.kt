package com.example.finalproject2.ui.main.main_tab_fragment.setting

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.finalproject2.R
import com.example.finalproject2.ui.main.MainNavigator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_personal.*

class SettingFragment : Fragment() {

    private val mPresenter by lazy { SettingPresenter() }
    lateinit var mNavigator: MainNavigator
    lateinit var mContext: Context

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("asd","setting")

        mNavigator = MainNavigator(mContext)

//        personalDetail.apply {
//            friendNameTextView.text = FirebaseAuthImpl.getUsername()
//            friendEmailTextView.text = FirebaseAuthImpl.getUserEmail()
//        }
//
//        Picasso
//            .get()
//            .load(FirebaseAuthImpl.getUserImageUri())
//            .into(personalDetail.friendAvatarImageView)
        
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
