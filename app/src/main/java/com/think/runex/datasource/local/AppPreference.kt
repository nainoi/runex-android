package com.think.runex.datasource.local

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.think.runex.common.toJson
import com.think.runex.common.toObject
import com.think.runex.config.KEY_ACCESS_TOKEN
import com.think.runex.config.KEY_FIREBASE_TOKEN
import com.think.runex.config.KEY_NIGHT_MODE
import com.think.runex.config.PREFERENCES_NAME
import com.think.runex.feature.auth.Token
import com.think.runex.utility.LocalManager.Companion.KEY_EN
import com.think.runex.utility.LocalManager.Companion.KEY_LANGUAGE

fun Context.getAppPreference(): SharedPreferences {
    return EncryptedSharedPreferences.create(PREFERENCES_NAME,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            this,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
}

fun SharedPreferences.getLanguageKey(): String = getString(KEY_LANGUAGE, KEY_EN) ?: KEY_EN

fun SharedPreferences.setLanguageKey(key: String) = edit {
    putString(KEY_LANGUAGE, key)
}

fun SharedPreferences.getNightMode(): Int = getInt(KEY_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

fun SharedPreferences.setNightMode(nightMode: Int) = edit {
    putInt(KEY_NIGHT_MODE, nightMode)
}

fun SharedPreferences.getAccessToken(): Token {
    return getString(KEY_ACCESS_TOKEN, "").toObject(Token::class.java) ?: Token()
}

fun SharedPreferences.setAccessToken(accessToken: Token) = edit {
    putString(KEY_ACCESS_TOKEN, accessToken.toJson())
}

fun SharedPreferences.getFirebaseToken(): String = getString(KEY_FIREBASE_TOKEN, "") ?: ""

fun SharedPreferences.setFirebaseToken(firebaseToken: String) = edit {
    putString(KEY_FIREBASE_TOKEN, firebaseToken)
}


