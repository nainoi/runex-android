package com.think.runex.feature.event.team.data

import com.google.gson.annotations.SerializedName

data class TeamImage(
    @SerializedName("reg_id") var registerId: String? = "",
    @SerializedName("icon_url") var iconUrl: String? = ""
)