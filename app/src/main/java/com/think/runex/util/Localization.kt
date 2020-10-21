package com.think.runex.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import android.util.Log
import androidx.core.content.edit
import java.util.*

class Localization {
    companion object {
        const val KEY_CURRENT_LANGUAGE = "current_language"
        const val THAI_LANGUAGE = "th"
        const val ENGLISH_LANGUAGE = "en"

        var CURRENT_LANGUAGE = ""
            private set

        fun getCurrentLanguage(context: Context): String {
            return AppPreference.createPreferenceNotEncrypt(context)
                    .getString(KEY_CURRENT_LANGUAGE, null) ?: ENGLISH_LANGUAGE
        }

        fun setCurrentLanguage(activity: Activity, language: String) {
            //AppPreference.setCurrentLanguage(activity, language)
            AppPreference.createPreferenceNotEncrypt(activity).edit {
                putString(KEY_CURRENT_LANGUAGE, language)
            }
            Locale.setDefault(Locale(language))
            activity.recreate()
        }

        fun isThaiLanguage(): Boolean = CURRENT_LANGUAGE == THAI_LANGUAGE

        fun applyLanguage(baseContext: Context?): Context? {
            if (baseContext == null) return baseContext

            val baseLanguage: String = getBaseLanguage(baseContext)
            val currentLanguage: String? = getCurrentLanguage(baseContext)

            if (currentLanguage?.isNotBlank() == true && currentLanguage.equals(baseLanguage, ignoreCase = true).not()) {
                val locale = Locale(currentLanguage)
                val config = Configuration(baseContext.resources.configuration)

                Locale.setDefault(locale)
                config.setLocale(locale)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val localeList = LocaleList(locale)
                    LocaleList.setDefault(localeList)
                    config.setLocales(localeList)
                }

                Log.d("Localization", "Set Language to: $currentLanguage")
                CURRENT_LANGUAGE = currentLanguage
                return baseContext.createConfigurationContext(config)
            } else {
                CURRENT_LANGUAGE = baseLanguage
                return baseContext
            }
        }

        private fun getBaseLanguage(context: Context): String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0).language
        } else {
            context.resources.configuration.locale.language
        }
    }
}