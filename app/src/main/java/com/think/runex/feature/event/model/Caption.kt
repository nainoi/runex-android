package com.think.runex.feature.event.model

import com.google.gson.annotations.SerializedName

data class Caption(
        @SerializedName("image") var image: String = "",
        @SerializedName("caption") var name: String = "")