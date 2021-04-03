package com.think.runex.feature.dashboard.data

import com.google.gson.annotations.SerializedName
import com.think.runex.util.extension.displayFormat
import com.think.runex.feature.event.data.EventDetail
import com.think.runex.feature.event.data.EventRegistered
import com.think.runex.feature.event.data.TicketCategory

data class DashboardInfo(
        @SerializedName("activities") var userActivityList: List<UserDashboard>? = null,
        @SerializedName("register") var eventRegistered: EventRegistered? = null) {

    fun getTotalDistanceDisplay(unit: String): String {
        var totalDistances = 0.0
        if (userActivityList?.isNotEmpty() == true) {
            totalDistances = userActivityList?.sumByDouble { it.totalDistances ?: 0.0 } ?: 0.0
        }
        return "${totalDistances.displayFormat()} $unit"
    }

    fun isEventCategoryTeam(): Boolean {
        return eventRegistered?.eventRegisteredList?.get(0)?.ticketOptions?.get(0)?.ticket?.category == TicketCategory.TEAM
    }
}