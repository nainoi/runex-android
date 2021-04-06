package com.think.runex.util

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.think.runex.BuildConfig
import com.think.runex.config.KEY_ENVIRONMENT
import com.think.runex.config.PREFERENCES_NAME
import com.think.runex.feature.setting.Environment

object AppPreference {

    fun isFirstOpen(context: Context): Boolean = createPreference(context).getBoolean("firstOpen", true)

    fun setFirstOpen(context: Context, isFirstOpen: Boolean) = createPreference(context).edit {
        putBoolean("firstOpen", isFirstOpen)
    }

    @Environment
    fun getEnvironment(context: Context): Int {
        return createPreference(context).getInt(KEY_ENVIRONMENT, if (BuildConfig.DEBUG) Environment.DEV else Environment.PRODUCTION)
    }

    fun setEnvironment(context: Context, @Environment environment: Int) = createPreference(context).edit {
        putInt(KEY_ENVIRONMENT, environment)
    }

    fun createPreferenceNotEncrypt(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun createPreference(context: Context): SharedPreferences {

        //TODO("Old")
//        val masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
//                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
//                .build()
//        return EncryptedSharedPreferences.create(context, PREFERENCES_NAME, masterKey,
//                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

        val keyGenParameterSpec = KeyGenParameterSpec.Builder(PREFERENCES_NAME, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()

        val masterKeyAlias = MasterKey.Builder(context, PREFERENCES_NAME)
                .setKeyGenParameterSpec(keyGenParameterSpec)
                .build()

        return EncryptedSharedPreferences.create(context, BuildConfig.APP_SCHEME, masterKeyAlias,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
    }

    fun clear(context: Context) {
        createPreference(context).edit {
            clear()
        }
    }
}