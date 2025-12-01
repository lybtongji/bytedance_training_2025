package com.example.tt_ic_editing

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

object AppConfig {
    private const val PREF_NAME = "app_config"
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    var themeMode: Int
        get() = prefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_UNSPECIFIED)
        set(value) = prefs.edit { putInt("theme_mode", value) }
}