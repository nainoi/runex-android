package com.think.runex.feature.dashboard.data

import com.google.gson.annotations.SerializedName
import com.jozzee.android.core.datetime.dateTimeFormat
import com.think.runex.common.displayFormat
import com.think.runex.config.DISPLAY_DATE_FORMAT
import com.think.runex.config.DISPLAY_DATE_TIME_FORMAT_THREE_LETTERS_DATE_MONTH
import com.think.runex.config.SERVER_DATE_TIME_FORMAT

data class UserActivity(
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
        @SerializedName("updated_at") var updatedAt: String? = null) {

    fun getDistanceDisplay(unit: String): String = "${(distance ?: 0.0).displayFormat()} $unit"

    fun getActivityDateDisplay(): String {
        return "${activityDate?.dateTimeFormat(SERVER_DATE_TIME_FORMAT, DISPLAY_DATE_TIME_FORMAT_THREE_LETTERS_DATE_MONTH)}"
    }
}