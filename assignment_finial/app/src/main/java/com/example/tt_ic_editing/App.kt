package com.example.tt_ic_editing

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppConfig.init(this)
    }
}