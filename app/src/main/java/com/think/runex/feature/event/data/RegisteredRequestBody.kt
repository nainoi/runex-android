package com.think.runex.feature.event.data

import com.google.gson.annotations.SerializedName

data class RegisteredRequestBody(
        @SerializedName("event_code") var eventCode: String = "",
        @SerializedName("reg_id") var registerId: String = "",
        @SerializedName("parent_reg_id") var parentRegisterId: String = "")