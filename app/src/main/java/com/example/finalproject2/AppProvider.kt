package com.example.finalproject

fun component() = App.appComponent

fun signUpPresenter() = component().registerPresenter()

fun signInPresenter() = component().loginPresenter()

fun mainPresenter() = component().mainPresenter()

fun contactPresenter() = component().contactPresenter()

fun personalPresenter() = component().personalPresenter()
