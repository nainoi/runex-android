package com.think.runex.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.think.runex.config.KEY_FIREBASE_TOKEN
import com.think.runex.config.PREFERENCES_NAME

object AppPreference {

    fun isFirstOpen(context: Context): Boolean = createPreference(context).getBoolean("firstOpen", true)

    fun setFirstOpen(context: Context, isFirstOpen: Boolean) = createPreference(context).edit {
        putBoolean("firstOpen", isFirstOpen)
    }

    fun createPreferenceNotEncrypt(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun createPreference(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        return EncryptedSharedPreferences.create(context, PREFERENCES_NAME, masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
    }

    fun clear(context: Context) {
        createPreference(context).edit {
            clear()
        }
    }
}