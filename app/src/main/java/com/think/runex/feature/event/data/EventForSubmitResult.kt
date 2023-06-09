package com.think.runex.feature.event.data

import com.google.gson.annotations.SerializedName

data class EventForSubmitResult(
        @SerializedName("event_code") var eventCode: String = "",
        @SerializedName("ticket") var ticket: Ticket = Ticket(),
        @SerializedName("order_id") var orderId: String = "",
        @SerializedName("reg_id") var registerId: String = "",
        @SerializedName("parent_reg_id") var parentRegisterId: String = "")
