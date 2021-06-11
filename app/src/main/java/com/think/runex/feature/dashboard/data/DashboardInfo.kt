package com.think.runex.feature.dashboard.data

import com.google.gson.annotations.SerializedName
import com.think.runex.util.extension.displayFormat
import com.think.runex.feature.event.data.Registered
import com.think.runex.feature.event.data.RegisteredData
import com.think.runex.feature.event.data.TicketCategory

data class DashboardInfo(
    @SerializedName("activities") var activityList: ArrayList<UserActivityDashboard>? = null,
    @SerializedName("register") var registered: Registered? = null
) {

    fun getTotalDistanceDisplay(unit: String): String {
        var totalDistances = 0.0
        if (activityList?.isNotEmpty() == true) {
            totalDistances = activityList?.sumOf { it.totalDistances ?: 0.0 } ?: 0.0
        }
        return "${totalDistances.displayFormat()} $unit"
    }

    fun isEventCategoryTeam(): Boolean {
        return registered?.getTicketAtRegister()?.category == TicketCategory.TEAM
    }

    fun isTeamLeader(userId: String): Boolean {
        return registered?.registeredDataList?.find { it.userId == userId && it.isTeamLead == true } != null
    }

    fun getRegistrationDataForEdit(userId: String) = Registered().apply {
        this.ownerId = registered?.ownerId
        this.userCode = registered?.userCode
        this.eventCode = registered?.eventCode
        this.ref2 = registered?.ref2
        this.eventDetail = registered?.eventDetail
        this.registeredDataList = ArrayList<RegisteredData>().apply {
            registered?.registeredDataList?.find { it.userId == userId }?.also { registerData ->
                add(registerData)
            }
        }
    }

}