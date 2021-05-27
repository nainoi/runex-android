package com.think.runex.util

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.think.runex.R

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
            setNightModePreference(activity, nightMode)
            //AppCompatDelegate.setDefaultNightMode(nightMode)
            //activity.delegate.localNightMode = nightMode
            //activity.delegate.applyDayNight()
            activity.recreate()
        }

        /**
         * Default night mode is [AppCompatDelegate.MODE_NIGHT_YES]
         */
        fun getNightMode(context: Context): Int {
            return AppPreference.createPreferenceNotEncrypt(context).getInt(KEY_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_YES)
        }

        private fun setNightModePreference(context: Context, nightMode: Int) {
            AppPreference.createPreferenceNotEncrypt(context).edit {
                putInt(KEY_NIGHT_MODE, nightMode)
            }
        }

        /**
         * Return 'On' if night mode is yes or 'Off' if night mode is no
         */
        fun getDarkModeDisplay(context: Context) = when (isNightMode(context)) {
            true -> context.getString(R.string.on)
            false -> context.getString(R.string.off)
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
            if (nightMode != 0) {

                AppCompatDelegate.setDefaultNightMode(nightMode)

                val config = Configuration(baseContext.resources.configuration)
                config.uiMode = when (nightMode) {
                    AppCompatDelegate.MODE_NIGHT_YES -> Configuration.UI_MODE_NIGHT_YES
                    AppCompatDelegate.MODE_NIGHT_NO -> Configuration.UI_MODE_NIGHT_NO
                    else -> config.uiMode
                }

                //Log.d("UiMode", "Set ui mode: ${config.uiMode}")

                setNightModePreference(baseContext, nightMode)
                return baseContext.createConfigurationContext(config)
            }
            return baseContext
        }
    }
}