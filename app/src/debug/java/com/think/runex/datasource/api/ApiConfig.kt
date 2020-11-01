package com.think.runex.datasource.api

class ApiConfig {
    companion object {
        const val AUTH_URL = "https://auth.runex.co"
        const val BASE_URL: String = "https://runex-api.thinkdev.app"
        const val EVENT_PREVIEW_URL = "https://runex.thinkdev.app/mpreview"
        const val API_VERSION: String = "v2"

        const val REFRESH_TOKEN_URL = "${BASE_URL}/api/v2/refreshAccessToken"
    }
}