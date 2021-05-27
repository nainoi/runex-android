package com.think.runex.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import android.util.Log
import androidx.core.content.edit
import com.think.runex.R
import java.util.*

class Localization {
    companion object {
        private const val KEY_CURRENT_LANGUAGE = "current_language"

        const val THAI_LANGUAGE = "th"
        const val ENGLISH_LANGUAGE = "en"

        var CURRENT_LANGUAGE = ""
            private set

        fun getCurrentLanguage(context: Context): String {
            CURRENT_LANGUAGE = getCurrentLanguagePreference(context)
            return CURRENT_LANGUAGE
        }

        fun setCurrentLanguage(activity: Activity, language: String) {
            CURRENT_LANGUAGE = language
            setCurrentLanguagePreference(activity, language)
            Locale.setDefault(Locale(language))
            activity.recreate()
        }

        fun setCurrentLanguageDisplay(context: Context) = when (getCurrentLanguage(context)) {
            THAI_LANGUAGE -> context.getString(R.string.thai_language)
            else -> context.getString(R.string.english_language)
        }

        fun applyLanguage(baseContext: Context?): Context? {
            if (baseContext == null) return baseContext

            val baseLanguage: String = getBaseLanguage(baseContext)
            val currentLanguage: String = getCurrentLanguage(baseContext)

            //Log.e("Localization", "Base language: $baseLanguage")
            //Log.e("Localization", "Current language: $currentLanguage")

            if (currentLanguage.equals(baseLanguage, ignoreCase = true).not()) {
                val locale = Locale(currentLanguage)
                val config = Configuration(baseContext.resources.configuration)

                Locale.setDefault(locale)
                config.setLocale(locale)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val localeList = LocaleList(locale)
                    LocaleList.setDefault(localeList)
                    config.setLocales(localeList)
                }

                //Log.d("Localization", "Set Language to: $currentLanguage")
                CURRENT_LANGUAGE = currentLanguage
                setCurrentLanguagePreference(baseContext, currentLanguage)
                return baseContext.createConfigurationContext(config)

            } else {
                return baseContext
            }
        }

        private fun getCurrentLanguagePreference(context: Context): String {
            return AppPreference.createPreferenceNotEncrypt(context)
                    .getString(KEY_CURRENT_LANGUAGE, null) ?: getBaseLanguage(context)
        }

        private fun setCurrentLanguagePreference(context: Context, language: String) {
            AppPreference.createPreferenceNotEncrypt(context).edit {
                putString(KEY_CURRENT_LANGUAGE, language)
            }
        }

        /**
         * Get device language.
         */
        fun getBaseLanguage(context: Context): String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0).language
        } else {
            context.resources.configuration.locale.language
        }
    }
}