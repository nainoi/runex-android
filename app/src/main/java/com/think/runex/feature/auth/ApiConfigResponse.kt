package com.think.runex.feature.auth

import com.google.gson.annotations.SerializedName

data class ApiConfigResponse(
        @SerializedName("leader_board_url") var leaderBoardUrl: String? = null,
        @SerializedName("authen_url") var loginUrl: String? = null,
        @SerializedName("authen_token") var authTokenUrl: String? = null,
        @SerializedName("preview_url") var previewEventUrl: String? = null)