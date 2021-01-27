package com.think.runex.feature.event.model

import com.google.gson.annotations.SerializedName

data class Shirts(
        @SerializedName("chest") var chest: String? = null,
        @SerializedName("id") var id: Int? = 0,
        @SerializedName("length") var length: String? = null,
        @SerializedName("short_sleeve_shirt") var shortSleeveShirt: Boolean? = false,
        @SerializedName("size") var size: String? = null,
        @SerializedName("sleeveless_shirt") var sleevelessShirt: Boolean? = false)