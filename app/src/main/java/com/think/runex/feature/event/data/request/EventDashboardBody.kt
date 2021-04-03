package com.think.runex.feature.event.data.request

import com.google.gson.annotations.SerializedName

data class EventDashboardBody(
        @SerializedName("event_code") var eventCode: String = "",
        @SerializedName("order_id") var orderId: String = "",
        @SerializedName("reg_id") var registerId: String = "",
        @SerializedName("parent_reg_id") var parentRegisterId: String = "")