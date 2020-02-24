package com.example.finalproject2.base

interface BasePresenter<in T> {
    fun setView(view: T)
    fun onStart()
    fun onStop()
}
