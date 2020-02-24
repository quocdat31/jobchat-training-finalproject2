package com.example.finalproject2.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.finalproject2.R
import com.example.finalproject2.mainPresenter
import com.example.finalproject2.model.Friend
import com.example.finalproject2.ui.main.main_tab_fragment.chat.ChatFragment
import com.example.finalproject2.ui.main.main_tab_fragment.contact.ContactFragment
import com.example.finalproject2.ui.main.main_tab_fragment.home.HomeFragment
import com.example.finalproject2.ui.main.main_tab_fragment.personal.PersonalFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home.*

class MainActivity : AppCompatActivity(),
    MainContract.View,
    ContactFragment.OnFragmentInteractionListener {

    companion object {
        fun getInstance(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private val mPresenter by lazy { mainPresenter() }
    var mNavigator = MainNavigator(this)
    private lateinit var mNavListener: BottomNavigationView.OnNavigationItemSelectedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mNavListener = initNavListener()
        homeBottomNavigation.setOnNavigationItemSelectedListener(mNavListener)
        mPresenter.setView(this)
    }

    private fun initNavListener(): BottomNavigationView.OnNavigationItemSelectedListener {
        return BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_home -> selectedFragment =
                    HomeFragment()
                R.id.nav_chat -> selectedFragment =
                    ChatFragment()
                R.id.nav_personal -> selectedFragment =
                    PersonalFragment()
                R.id.nav_contact -> selectedFragment = ContactFragment()
            }
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    selectedFragment
                ).commit()
            }
            true
        }
    }

    override fun onFragmentInteraction(friend: Friend) {
        Log.d("asd", friend.toString())
    }
}
