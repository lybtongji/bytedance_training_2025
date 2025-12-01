package com.example.tt_ic_editing

import android.app.Activity
import androidx.appcompat.app.AppCompatDelegate
import java.lang.ref.WeakReference

object ThemeManager {
    private const val PREF_NAME = "theme_prefs"
    private const val KEY_THEME_MODE = "theme_mode"
    private val activities = mutableListOf<WeakReference<Activity>>()

    @Volatile
    private var themeMode: Int? = null

    fun register(activity: Activity) {
        activities.add(WeakReference(activity))
    }

    fun unregister(activity: Activity) {
        activities.removeAll { it.get() == activity || it.get() == null }
    }

    fun setThemeMode(mode: Int) {
        synchronized(this) {
            val currentMode = themeMode ?: AppConfig.themeMode
            if (mode != currentMode) {
                AppConfig.themeMode = mode
                themeMode = mode
                apply()
            }
        }
    }

    fun apply() {
        val mode = themeMode ?: return
        synchronized(this) {
            AppCompatDelegate.setDefaultNightMode(mode)
            activities.removeAll { it.get() == null }
            activities.forEach { it.get()?.recreate() }
        }
    }
}