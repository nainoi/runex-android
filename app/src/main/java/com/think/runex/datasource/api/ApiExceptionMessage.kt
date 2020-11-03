package com.think.runex.datasource.api

import android.content.res.Resources
import com.think.runex.R
import com.think.runex.util.ERR_NO_INTERNET_CONNECTION
import javax.net.ssl.HttpsURLConnection

class ApiExceptionMessage {
    companion object {
        fun getExceptionMessageFromStatusCode(resource: Resources, statusCode: Int, defaultMessage: String): String = when (statusCode) {
            ERR_NO_INTERNET_CONNECTION -> resource.getString(R.string.no_internet_connection)
            HttpsURLConnection.HTTP_CLIENT_TIMEOUT -> resource.getString(R.string.connection_timed_out)
            HttpsURLConnection.HTTP_UNAUTHORIZED -> resource.getString(R.string.please_login)
            else -> defaultMessage
        }
    }
}