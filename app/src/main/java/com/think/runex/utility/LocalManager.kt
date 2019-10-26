package com.think.runex.utility

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.LocaleList
import android.util.Log
import com.think.runex.datasource.local.getAppPreference
import com.think.runex.datasource.local.getLanguageKey
import com.think.runex.datasource.local.setLanguageKey
import java.util.*

class LocalManager {
    companion object {
        const val KEY_LANGUAGE = "language"
        const val KEY_TH = "th"
        const val KEY_EN = "en"  //en_US,

        private var instance: LocalManager? = null

        fun getInstance(): LocalManager {
            if (instance == null) {
                instance = LocalManager()
            }
            return instance!!
        }

        /**
         * Return current language in application,
         * Must be [initialLanguage] before.
         */
        var currentLanguage: String = ""
            private set
            get() {
                if (field.isBlank()) {
                    Log.d("LocalManager", "Not initial language? Make sure your set initialLanguage")
                }
                return field
            }

        /**
         * Returns `true` if [currentLanguage] contains [KEY_TH] (`th`)
         */
        fun isThaiLang() = currentLanguage.contains(KEY_TH, false)
    }

    /**
     * Get application language.
     *
     * @return [Locale] at set in application.
     */
    fun getLanguage(): Locale = Locale.getDefault()

    /**
     * Set Set language to shared preferences
     * and recreate activity for update locale [Wrapper.wrapLanguage] on attachBaseContext.
     */
    fun setLanguage(activity: Activity, language: String) {
        currentLanguage = language
        activity.getAppPreference().setLanguageKey(language)
        activity.recreate()
    }

    /**
     * Get system language
     *
     * @return [Locale] at system language.
     */
    fun getDeviceLanguage(context: Context): Locale {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0].language
        }
        @Suppress("DEPRECATION")
        return context.resources.configuration.locale
    }

    /**
     * Set language to shared preferences and update configuration
     * After set completed should be recreate activity.
     */
    /** @deprecated */
    @Deprecated("Use setLanguage instead.")
    fun setApplicationLanguage(context: Context, language: String) {
        currentLanguage = language
        context.getAppPreference().setLanguageKey(language)
    }

    /**
     * Get language from application and update to [currentLanguage]
     */
    fun initialLanguage() {
        currentLanguage = getLanguage().language
    }

    class Wrapper(var base: Context?) : ContextWrapper(base) {

        fun wrapLanguage(defaultLang: String = ""): ContextWrapper {
            if (base != null) {
                val lang = when (defaultLang.isBlank()) {
                    true -> base!!.getAppPreference().getLanguageKey()
                    false -> defaultLang
                }
                if (lang.isNotBlank()) {
                    Log.d("LocalWrapper", "Set Language to: $lang")

                    val resources = base!!.resources
                    val config = resources.configuration
                    val newLocale = Locale(lang)

                    currentLanguage = lang
                    config.setLocale(newLocale)
                    Locale.setDefault(newLocale)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        val localeList = LocaleList(newLocale)
                        LocaleList.setDefault(localeList)
                        config.setLocales(localeList)
                    }
                    base = base!!.createConfigurationContext(config)
                    return ContextWrapper(base)
                }
            }
            return ContextWrapper(base)
        }
    }
}