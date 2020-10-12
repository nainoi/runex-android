package com.think.runex.common

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class UiMode {
    companion object {
        const val KEY_UI_MODE = "ui_mode"

        /**
         * Set [AppCompatDelegate.MODE_NIGHT_YES],
         * [AppCompatDelegate.MODE_NIGHT_NO],
         * [AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM],
         * [AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY]
         * [AppCompatDelegate.MODE_NIGHT_UNSPECIFIED]
         */
        fun setNightMode(activity: AppCompatActivity, nightMode: Int) {
            AppPreference.setCurrentNightMode(activity, nightMode)
            //AppCompatDelegate.setDefaultNightMode(nightMode)
            //activity.delegate.localNightMode = nightMode
            //activity.delegate.applyDayNight()
            activity.recreate()
        }

        fun applyUiMode(baseContext: Context?): Context? {
            if (baseContext == null) return baseContext
            val nightMode = AppPreference.getCurrentNightMode(baseContext)
            if (nightMode != 0) {
                AppCompatDelegate.setDefaultNightMode(nightMode)
                val config = Configuration(baseContext.resources.configuration)
                config.uiMode = when (nightMode) {
                    AppCompatDelegate.MODE_NIGHT_YES -> Configuration.UI_MODE_NIGHT_YES
                    AppCompatDelegate.MODE_NIGHT_NO -> Configuration.UI_MODE_NIGHT_NO
                    else -> config.uiMode
                }
                Log.d("UiMode", "Set ui mode: ${config.uiMode}")
                return baseContext.createConfigurationContext(config)
            }
            return baseContext
        }
    }
}