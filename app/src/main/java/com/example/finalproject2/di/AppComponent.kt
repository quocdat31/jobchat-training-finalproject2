package com.example.finalproject2.di

import dagger.Component
import javax.inject.Singleton

@Component(modules = [PresenterModule::class])
@Singleton
interface AppComponent {

    fun signUpPresenter(): RegisterContract.Presenter

    fun signInPresenter(): LogInContract.Presenter

    fun mainPresenter(): MainContract.Presenter

    fun contactPresenter(): ContactContract.Presenter

    fun personalPresenter(): PersonalContract.Presenter

}
