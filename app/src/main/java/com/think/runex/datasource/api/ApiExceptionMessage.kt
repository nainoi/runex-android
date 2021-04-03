package com.think.runex.datasource.api

import android.content.res.Resources
import com.google.gson.JsonSyntaxException
import com.think.runex.R
import com.think.runex.config.ERROR_JSON_FORMAT
import com.think.runex.config.ERR_NO_INTERNET_CONNECTION
import javax.net.ssl.HttpsURLConnection

class ApiExceptionMessage {
    companion object {
        fun getExceptionMessageFromStatusCode(resource: Resources, statusCode: Int, defaultMessage: String): String = when (statusCode) {
            ERR_NO_INTERNET_CONNECTION -> resource.getString(R.string.no_internet_connection)
            HttpsURLConnection.HTTP_CLIENT_TIMEOUT -> resource.getString(R.string.connection_timed_out)
            HttpsURLConnection.HTTP_UNAUTHORIZED -> resource.getString(R.string.please_login)
            HttpsURLConnection.HTTP_BAD_GATEWAY -> resource.getString(R.string.service_unavailable)
            ERROR_JSON_FORMAT -> resource.getString(R.string.invalid_data_format)
            else -> defaultMessage
        }
    }
}