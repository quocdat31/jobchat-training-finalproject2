package com.example.finalproject2

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.LocalDateTime

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}
