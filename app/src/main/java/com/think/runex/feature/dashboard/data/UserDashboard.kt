package com.think.runex.feature.dashboard.data

import com.google.gson.annotations.SerializedName
import com.think.runex.util.extension.displayFormat
import com.think.runex.feature.activity.data.ActivityInfo
import com.think.runex.feature.event.data.Ticket

data class UserDashboard(
        @SerializedName("id") var id: String? = null,
        @SerializedName("event_code") var eventCode: String? = null,
        @SerializedName("ticket") var ticket: Ticket? = null,
        @SerializedName("order_id") var orderId: String? = null,
        @SerializedName("reg_id") var registerId: String? = null,
        @SerializedName("parent_reg_id") var parentRegisterId: String? = null,
        @SerializedName("user_id") var userId: String? = null,
        @SerializedName("total_distance") var totalDistances: Double? = 0.0,
        @SerializedName("activity_info") var activityInfo: List<ActivityInfo>? = null) {

    fun getTotalDistanceDisplay(unit: String): String = "${(totalDistances ?: 0.0).displayFormat()} $unit"
}