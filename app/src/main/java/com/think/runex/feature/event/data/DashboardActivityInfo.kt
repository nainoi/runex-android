package com.think.runex.feature.event.data

import com.google.gson.annotations.SerializedName

data class DashboardActivityInfo(
        @SerializedName("id") var id: String? = null,
        @SerializedName("distance") var distance: Double? = 0.0,
        @SerializedName("img_url") var imgUrl: String? = null,
        @SerializedName("caption") var caption: String? = null,
        @SerializedName("app") var app: String? = null,
        @SerializedName("time") var time: Long? = 0,
        @SerializedName("is_approve") var isApprove: Boolean? = false,
        @SerializedName("status") var status: String? = null,
        @SerializedName("activity_date") var activityDate: String? = null,
        @SerializedName("created_at") var createdAt: String? = null,
        @SerializedName("updated_at") var updatedAt: String? = null)