package com.think.runex.feature.event.model

import com.google.gson.annotations.SerializedName

data class Category(
        @SerializedName("id") var id: String = "",
        @SerializedName("name") var name: String = "",
        @SerializedName("active") var isActive: Boolean = false)