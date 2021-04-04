package com.think.runex.util.extension

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

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

fun JsonObject.toRequestBody(): RequestBody {
    return toJson().toRequestBody("application/json; charset=utf-8".toMediaType())
}