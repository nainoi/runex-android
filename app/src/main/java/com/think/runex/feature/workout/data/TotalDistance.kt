package com.think.runex.feature.workout.data

import com.google.gson.annotations.SerializedName

data class TotalDistance(
        @SerializedName("user_id") var userId: String? = null,
        @SerializedName("total_distance") var totalDistance: Double? = 0.0)