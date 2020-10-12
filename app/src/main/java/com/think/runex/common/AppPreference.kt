package com.think.runex.common

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.think.runex.common.UiMode.Companion.KEY_UI_MODE
import com.think.runex.config.KEY_FIREBASE_TOKEN
import com.think.runex.config.PREFERENCE_NAME
import java.util.*

object AppPreference {

    fun getFirebaseToken(context: Context): String? {
        return createPreferenceEncrypted(context).getString(KEY_FIREBASE_TOKEN, null)
    }

    fun setFirebaseToken(context: Context, firebaseToken: String) {
        createPreferenceEncrypted(context).edit {
            putString(KEY_FIREBASE_TOKEN, firebaseToken)
        }
    }

    fun removeFireBaseToken(context: Context) = createPreferenceEncrypted(context).edit {
        remove(KEY_FIREBASE_TOKEN)
    }

    /*fun getCurrentLanguage(context: Context): String {
        //TODO("Can not change ui mode when set language th or en")
        return createPreference(context).getString(Localization.KEY_CURRENT_LANGUAGE, null)
                ?: Localization.THAI_LANGUAGE//Locale.getDefault().language
    }

    fun setCurrentLanguage(context: Context, language: String) {
        createPreference(context).edit {
            putString(Localization.KEY_CURRENT_LANGUAGE, language)
        }
    }*/

    /**
     * Return [AppCompatDelegate.MODE_NIGHT_YES],
     * [AppCompatDelegate.MODE_NIGHT_NO],
     * [AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM],
     * [AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY]
     * [AppCompatDelegate.MODE_NIGHT_UNSPECIFIED]
     * by default is 0
     */
    fun getCurrentNightMode(context: Context): Int {
        return createPreference(context).getInt(KEY_UI_MODE, 0)
    }

    fun setCurrentNightMode(context: Context, nightMode: Int) {
        createPreference(context).edit {
            putInt(KEY_UI_MODE, nightMode)
        }
    }

    fun isNightMode(context: Context): Boolean {
        return getCurrentNightMode(context).let { mode ->
            (mode == AppCompatDelegate.MODE_NIGHT_YES ||
                    ((mode == 0 || mode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) &&
                            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES))
        }
    }

    fun isFirstOpen(context: Context): Boolean = createPreference(context).getBoolean("firstOpen", true)

    fun setFirstOpen(context: Context, isFirstOpen: Boolean) = createPreference(context).edit {
        putBoolean("firstOpen", isFirstOpen)
    }

    fun createPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun createPreferenceEncrypted(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        return EncryptedSharedPreferences.create(context, PREFERENCE_NAME, masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
    }

    fun clear(context: Context) {
        createPreferenceEncrypted(context).edit {
            clear()
        }
    }
}