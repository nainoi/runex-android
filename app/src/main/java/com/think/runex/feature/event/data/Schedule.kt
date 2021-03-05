package com.think.runex.feature.event.data

import com.google.gson.annotations.SerializedName

data class Schedule(
        @SerializedName("description") var description: String? = null,
        @SerializedName("id") var id: Int? = 0,
        @SerializedName("name") var name: String? = null)