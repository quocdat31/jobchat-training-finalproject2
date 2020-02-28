package com.example.finalproject2.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.finalproject2.R
import com.example.finalproject2.firebase.authentication.FirebaseAuthImpl
import com.example.finalproject2.model.User
import com.example.finalproject2.ui.conversation.ConversationActivity
import com.example.finalproject2.ui.main.main_tab_fragment.chat.ChatFragment
import com.example.finalproject2.ui.main.main_tab_fragment.contact.ContactFragment
import com.example.finalproject2.ui.main.main_tab_fragment.setting.SettingFragment
import com.example.finalproject2.ultis.extension.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rhexgomez.typer.roboto.TyperRoboto
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.collapsing_toolbar.*


class MainActivity : AppCompatActivity(),
    MainContract.View,
    ContactFragment.OnFragmentInteractionListener {

    companion object {
        fun getInstance(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    val EXTRA_KEY = "contact"
    private val CONTACT_TITLE = "Contact"
    private val CHAT_TITLE = "Chat"
    private val COLOR_WHITE = "white"

    private val CHAT_TAB_POSITION = 0
    private val CONTACT_TAB_POSITION = 1
    private val SETTING_TAB_POSITION = 2

    private val mPresenter by lazy { MainPresenter() }
    var mNavigator = MainNavigator(this)
    private lateinit var mNavListener: BottomNavigationView.OnNavigationItemSelectedListener
    private lateinit var mTextViewQueryListener: SearchView.OnQueryTextListener
    lateinit var mMenuItem: MenuItem
    lateinit var mContactListener: MainInteractionListener.ContactListener
    lateinit var mSearchView: SearchView

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initToolBar()
        mTextViewQueryListener = initQueryTextListener()
        mNavListener = initNavListener()
        homeBottomNavigation.setOnNavigationItemSelectedListener(mNavListener)
        mainViewPager.addOnPageChangeListener(initMainViewPagerListener())
        mPresenter.setView(this)
        initMainViewPager()

    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        mContactListener = ContactFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar_menu, menu)
        val searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        mSearchView = menu?.findItem(R.id.app_bar_search)?.actionView as SearchView
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        mSearchView.setOnQueryTextListener(mTextViewQueryListener)
        return true
    }

    private fun initMainViewPager() {
        val fragmentList = arrayListOf<Fragment>()
        fragmentList.add(ChatFragment())
        fragmentList.add(ContactFragment())
        fragmentList.add(SettingFragment())
        mainViewPager.adapter = MainViewPagerAdapter(supportFragmentManager, fragmentList)
    }


    private fun initMainViewPagerListener(): ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) = Unit
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) = Unit

            override fun onPageSelected(position: Int) {
                mMenuItem = homeBottomNavigation.menu.getItem(position)
                homeBottomNavigation.menu.getItem(position).isChecked = true
                when (position) {
                    CHAT_TAB_POSITION -> onChatTabSelected()
                    CONTACT_TAB_POSITION -> onContactTabSelected()
                    else -> onPersonalTabSelected()
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initNavListener(): BottomNavigationView.OnNavigationItemSelectedListener {
        return BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_chat -> {
                    mainViewPager.currentItem = CHAT_TAB_POSITION
                }
                R.id.nav_contact -> {
                    mainViewPager.currentItem = CONTACT_TAB_POSITION
                }
                else -> {
                    mainViewPager.currentItem = SETTING_TAB_POSITION
                }
            }
            true
        }
    }

    private fun initQueryTextListener(): SearchView.OnQueryTextListener =
        object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mPresenter.addFriend(query.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        }

    private fun onChatTabSelected() {
        mainCollapsingAppBar.setExpanded(false, true)
        mainCollapsingToolbar.apply {
            title = CHAT_TITLE
        }
    }

    private fun onContactTabSelected() {
        mainCollapsingAppBar.setExpanded(false, true)
        mainCollapsingToolbar.apply {
            title = CONTACT_TITLE
        }
    }

    private fun onPersonalTabSelected() {
        mainCollapsingAppBar.setExpanded(true, true)
        mainCollapsingToolbar.apply {
            title = FirebaseAuthImpl.getUsername()
        }
    }

    private fun initToolBar() {
        setSupportActionBar(mainToolBarInsideCollapsingToolBar)
        supportActionBar?.setHomeButtonEnabled(true)
        mainCollapsingAppBar.setExpanded(false, true)
        mainCollapsingToolbar?.apply {
            title = CHAT_TITLE
            setCollapsedTitleTypeface(TyperRoboto.ROBOTO_REGULAR)
            setExpandedTitleTypeface(TyperRoboto.ROBOTO_REGULAR)
            setCollapsedTitleTextColor(resources.getColor(R.color.colorWhite))
            setExpandedTitleColor(resources.getColor(R.color.colorWhite))
        }
    }

    override fun onFragmentInteraction(user: User) {
        val intent = ConversationActivity.getInstance(this)
        intent.putExtra(EXTRA_KEY, user)
        startActivity(intent)
    }

    override fun onAddFriendSuccess() {
        this.toast("Success")
        mSearchView.setQuery(null, false)
        mSearchView.clearFocus()
        mContactListener.onAddFriendSuccess("")
    }

    override fun onAddFriendError(error: String) {
        this.toast(error)
    }
}
