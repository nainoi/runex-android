package com.think.runex.config

import com.google.gson.annotations.SerializedName

data class AppConfig(
        @SerializedName("leader_board_url") var leaderBoardUrl: String? = null,
        @SerializedName("authen_url") var loginUrl: String? = null,
        @SerializedName("authen_token") var authTokenUrl: String? = null,
        @SerializedName("preview_url") var previewEventUrl: String? = null)