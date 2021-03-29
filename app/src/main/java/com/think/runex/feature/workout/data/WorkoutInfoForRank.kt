package com.think.runex.feature.workout.data

import com.google.gson.annotations.SerializedName

data class WorkoutInfoForRank(
        @SerializedName("id") var id: String? = null,
        @SerializedName("distance") var distanceKilometers: Float? = 0f,
        @SerializedName("img_url") var imgUrl: String? = null,
        @SerializedName("caption") var caption: String? = null,
        @SerializedName("app") var app: String? = null,
        @SerializedName("time_string") var time: Long? = null,
        @SerializedName("is_approve") var isApprove: Boolean? = false,
        @SerializedName("status") var status: String? = null,
        @SerializedName("activity_date") var activityDate: String? = null,
        @SerializedName("created_at") var createdAt: String? = null,
        @SerializedName("updated_at") var updatedAt: String? = null)