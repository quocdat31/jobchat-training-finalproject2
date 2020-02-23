package com.example.finalproject

import android.app.Application
import com.example.finalproject2.di.AppComponent
import com.example.finalproject.di.DaggerAppComponent

class App : Application() {

    companion object {
        lateinit var instance: App
                private set
        val appComponent: AppComponent by lazy {
            DaggerAppComponent.builder().build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}