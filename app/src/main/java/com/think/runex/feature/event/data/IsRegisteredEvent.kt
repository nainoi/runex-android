package com.think.runex.feature.event.data

import com.google.gson.annotations.SerializedName

data class IsRegisteredEvent(
        @SerializedName("is_reg") var isRegistered: Boolean? = false)