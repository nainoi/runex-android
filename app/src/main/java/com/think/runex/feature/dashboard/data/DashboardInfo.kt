package com.think.runex.feature.dashboard.data

import com.google.gson.annotations.SerializedName
import com.think.runex.common.displayFormat
import com.think.runex.feature.dashboard.data.DashboardActivityInfo
import com.think.runex.feature.event.data.Ticket

data class DashboardInfo(
        @SerializedName("id") var id: String? = null,
        @SerializedName("event_code") var eventCode: String? = null,
        @SerializedName("ticket") var ticket: Ticket? = null,
        @SerializedName("order_id") var orderId: String? = null,
        @SerializedName("reg_id") var regId: String? = null,
        @SerializedName("parent_reg_id") var parentRegId: String? = null,
        @SerializedName("user_id") var userId: String? = null,
        @SerializedName("total_distance") var totalDistance: Double? = 0.0,
        @SerializedName("activity_info") var activityInfoList: List<DashboardActivityInfo>? = null) {

    fun getTotalDistanceDisplay(unit: String): String {
        return "${(totalDistance ?: 0.0).displayFormat()} $unit"
    }
}