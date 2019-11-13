package com.think.runex.common

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.jozzee.android.core.utility.Logger
import com.think.runex.config.KEY_MESSAGE
import org.json.JSONException
import org.json.JSONObject

fun FuelError.getErrorMessage(): String {
    var errorMessage = errorData.toString(Charsets.UTF_8)
    if (errorMessage.isBlank()) {
        return errorMessage
    }
    try {
        val jsonObject = JSONObject(errorMessage)
        if (jsonObject.has(KEY_MESSAGE)) {
            errorMessage = jsonObject.getString(KEY_MESSAGE)
        }
    } catch (exception: JSONException) {
        exception.printStackTrace()
    }
    return errorMessage
}
