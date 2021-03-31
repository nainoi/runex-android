package com.think.runex.feature.dashboard.data

import com.google.gson.annotations.SerializedName
import com.think.runex.common.displayFormat
import com.think.runex.feature.event.data.EventDetail
import com.think.runex.feature.event.data.EventRegistered

data class DashboardInfo(
        @SerializedName("activities") var userActivityList: List<UserDashboard>? = null,
        @SerializedName("register") var eventRegistered: EventRegistered? = null,
        @SerializedName("event") var eventDetail: EventDetail? = null) {

    fun getTotalDistanceDisplay(unit: String): String {
        var totalDistances = 0.0
        if (userActivityList?.isNotEmpty() == true) {
            totalDistances = userActivityList?.sumByDouble { it.totalDistances ?: 0.0 } ?: 0.0
        }
        return "${totalDistances.displayFormat()} $unit"
    }

}