package com.think.runex.feature.event.data

import com.google.gson.annotations.SerializedName
import com.think.runex.common.displayFormat

data class EventDashboard(
        @SerializedName("id") var id: String? = null,
        @SerializedName("event_id") var eventId: String? = null,
        @SerializedName("event_code") var eventCode: String? = null,
        @SerializedName("activities") var activity: Activity? = null) {


    fun getTotalDistance(): Double {
        return activity?.totalDistance ?: 0.0
    }

    fun getTotalDistance(unit: String): String {
        return ("${(activity?.totalDistance ?: 0.0).displayFormat(awaysShowDecimal = true)} $unit")
    }

    data class Activity(
            @SerializedName("user_id") var userId: String? = null,
            @SerializedName("total_distance") var totalDistance: Double? = 0.0)
}