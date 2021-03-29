package com.think.runex.feature.event.data

import com.google.gson.annotations.SerializedName
import com.think.runex.common.displayFormat

data class EventDashboard(
        @SerializedName("event") var event: EventDetail? = null,
        @SerializedName("ranks") var ranks: List<Rank>? = null,
        @SerializedName("myrank") var myRank: List<Rank>? = null,
        @SerializedName("total_activity") var totalActivity: Int? = null) {

    fun getEventName() = event?.title ?: ""

    fun getTotalDistance(): Double {
        return myRank?.get(0)?.totalDistance ?: 0.0
    }

    fun getTotalDistance(unit: String): String {
        return ("${getTotalDistance().displayFormat(awaysShowDecimal = true)} $unit")
    }

    data class Activity(
            @SerializedName("user_id") var userId: String? = null,
            @SerializedName("total_distance") var totalDistance: Double? = 0.0)
}