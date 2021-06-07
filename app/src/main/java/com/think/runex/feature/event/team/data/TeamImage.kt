package com.think.runex.feature.event.team.data

import com.google.gson.annotations.SerializedName

data class TeamImage(
    @SerializedName("reg_id") val registerId: String? = "",
    @SerializedName("icon_url") val iconUrl: String? = ""
)