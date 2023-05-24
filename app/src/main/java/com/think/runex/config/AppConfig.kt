package com.think.runex.config

import com.google.gson.annotations.SerializedName

data class AppConfig(
        @SerializedName("leader_board_url") var leaderBoardUrl: String? = null,
        @SerializedName("create_user_url") var createUserUrl: String? = null,
        @SerializedName("authen_url") var loginUrl: String? = null,
        @SerializedName("authen_token") var authTokenUrl: String? = null,
        @SerializedName("preview_url") var previewEventUrl: String? = null,
        @SerializedName("qr_url") var qrUrl: String? = null,
        @SerializedName("qrcode_url") var qrCodeUrl: String? = null,
        @SerializedName("konex_url") var konexUrl: String? = null,
        @SerializedName("events_mobile_url") var eventsMobileUrl: String? = "https://runex-my-next.thinkdev.app/mobile/events?code=384f05e87c133740071a809a29b65905b55a8859")