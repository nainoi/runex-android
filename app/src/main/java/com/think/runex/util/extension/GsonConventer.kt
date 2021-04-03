package com.think.runex.util.extension

import com.google.gson.Gson

/**
 * Convert json string to any object!
 */
fun <T> String?.toObject(ofClass: Class<T>): T? {
    try {
        if (this != null && this.isNotBlank()) {
            return Gson().fromJson(this, ofClass)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

/**
 * Convert any object to json string.
 */
fun <T> T.toJson(): String {
    try {
        return Gson().toJson(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}