package com.think.runex.datasource.api

import com.think.runex.BuildConfig
import com.think.runex.config.AppConfig

class ApiConfig {
    companion object {
        const val API_VERSION = "v2"

        var BASE_URL: String = "https://api.runex.co"
            private set
            get() = when (BuildConfig.DEBUG) {
                true -> "https://api.runex.co"//"https://runex-api.thinkdev.app" //"https://api.runex.co"
                false -> "https://api.runex.co"
            }

        var AUTH_URL = "https://auth.runex.co/v1/oauth2/token"
            private set

        val REFRESH_TOKEN_URL = "${BASE_URL}/api/v2/refreshAccessToken"

        var LOGIN_URL: String = "https://auth.runex.co/login"
            private set

        var LEADER_BOARD_URL = "https://leaderboard.runex.co"
            private set

        var PREVIEW_EVENT_URL = "https://runex.thinkdev.app/m/preview"
            private set

        var QR_URL = "https://payment.runex.co/PromptPay/Think"
            private set

        var QR_CODE_URL = "https://runex-payments.thinkdev.app/scb/v1/create-qrcode"
            private set

        var KONEX_URL = "https://konex.thinkdev.app/settings"
            private set


        fun updateAppConfig(config: AppConfig?) {
            if (config == null) return
            AUTH_URL = config.authTokenUrl ?: AUTH_URL
            LOGIN_URL = config.loginUrl ?: LOGIN_URL
            LEADER_BOARD_URL = config.leaderBoardUrl ?: LEADER_BOARD_URL
            PREVIEW_EVENT_URL = config.previewEventUrl ?: PREVIEW_EVENT_URL
            QR_URL = config.qrUrl ?: QR_URL
            QR_CODE_URL = config.qrCodeUrl ?: QR_CODE_URL
        }
    }
}