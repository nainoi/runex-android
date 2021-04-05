package com.think.runex.feature.dashboard.data

import com.google.gson.annotations.SerializedName
import com.think.runex.util.extension.displayFormat
import com.think.runex.feature.event.data.Registered
import com.think.runex.feature.event.data.TicketCategory

data class DashboardInfo(
        @SerializedName("activities") var userActivityList: List<UserDashboard>? = null,
        @SerializedName("register") var registered: Registered? = null) {

    fun getTotalDistanceDisplay(unit: String): String {
        var totalDistances = 0.0
        if (userActivityList?.isNotEmpty() == true) {
            totalDistances = userActivityList?.sumByDouble { it.totalDistances ?: 0.0 } ?: 0.0
        }
        return "${totalDistances.displayFormat()} $unit"
    }

    fun isEventCategoryTeam(): Boolean {
        return registered?.getTicketAtRegister()?.category == TicketCategory.TEAM
    }

    fun isTeamLeader(userId: String): Boolean {
        return registered?.registeredDataList?.find { it.userId == userId && it.isTeamLead == true } != null
    }
}