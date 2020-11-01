package com.think.runex.feature.event.model.response

import com.google.gson.annotations.SerializedName

data class IsRegisteredEventResponse(
        @SerializedName("is_reg") var isRegistered: Boolean = false)