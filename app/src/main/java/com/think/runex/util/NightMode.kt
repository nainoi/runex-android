package com.think.runex.util

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

class NightMode {
    companion object {
        const val KEY_NIGHT_MODE = "night_mode"

        /**
         * Set [AppCompatDelegate.MODE_NIGHT_YES],
         * [AppCompatDelegate.MODE_NIGHT_NO],
         * [AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM],
         * [AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY]
         * [AppCompatDelegate.MODE_NIGHT_UNSPECIFIED]
         */
        fun setNightMode(activity: AppCompatActivity, nightMode: Int) {
            AppPreference.createPreferenceNotEncrypt(activity).edit {
                putInt(KEY_NIGHT_MODE, nightMode)
            }
            //AppCompatDelegate.setDefaultNightMode(nightMode)
            //activity.delegate.localNightMode = nightMode
            //activity.delegate.applyDayNight()
            activity.recreate()
        }

        fun getNightMode(context: Context): Int? {
            //TODO("Default night mode is no")
            return AppPreference.createPreferenceNotEncrypt(context).getInt(KEY_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_NO)
        }

        fun isNightMode(context: Context): Boolean {
            return getNightMode(context).let { mode ->
                (mode == AppCompatDelegate.MODE_NIGHT_YES ||
                        ((mode == 0 || mode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) &&
                                context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES))
            }
        }

        fun applyNightMode(baseContext: Context?): Context? {
            if (baseContext == null) return baseContext
            val nightMode = getNightMode(baseContext)
            if (nightMode != null && nightMode != 0) {
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