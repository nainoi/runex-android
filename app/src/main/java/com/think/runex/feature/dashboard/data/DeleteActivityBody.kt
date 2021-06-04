package com.think.runex.feature.dashboard.data

import com.google.gson.annotations.SerializedName
import com.think.runex.feature.activity.data.ActivityInfo

data class DeleteActivityBody(
    @SerializedName("activity_info") var activityInfo: ActivityInfo = ActivityInfo(),
    @SerializedName("event_code") var eventCode: String = "",
    @SerializedName("order_id") var orderId: String = "",
    @SerializedName("reg_id") var registerId: String = "",
    @SerializedName("parent_reg_id") var parentRegisterId: String = "",
    @SerializedName("ticket_id") var ticketId: String = ""
)
