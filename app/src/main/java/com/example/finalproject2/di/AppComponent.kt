package com.example.finalproject2.di

import com.example.finalproject2.ui.main.MainContract
import com.example.finalproject2.ui.main.main_tab_fragment.contact.ContactContract
import com.example.finalproject2.ui.main.main_tab_fragment.personal.PersonalContract
import com.example.finalproject2.ui.signin.SignInContract
import com.example.finalproject2.ui.signup.SignUpContract
import dagger.Component
import javax.inject.Singleton

@Component(modules = [PresenterModule::class])
@Singleton
interface AppComponent {
    fun signUpPresenter(): SignUpContract.Presenter
    fun signInPresenter(): SignInContract.Presenter
    fun mainPresenter(): MainContract.Presenter
    fun contactPresenter(): ContactContract.Presenter
    fun personalPresenter(): PersonalContract.Presenter
}
