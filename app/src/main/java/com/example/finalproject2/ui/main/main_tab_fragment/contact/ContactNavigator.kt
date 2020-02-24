package com.example.finalproject2.ui.main.main_tab_fragment.contact

import android.content.Context
import com.example.finalproject2.base.BaseNavigator
import com.example.finalproject2.ui.conversation.ConversationActivity

class ContactNavigator(context: Context) : BaseNavigator(context),
    ContactContract.Navigator {
    override fun navigateChatActivity() {
        context.startActivity(ConversationActivity.getInstance(context))
    }
}
