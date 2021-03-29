package com.think.runex.feature.event.data

import com.google.gson.annotations.SerializedName
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.feature.workout.data.WorkoutInfoForRank

data class Rank(
        @SerializedName("id") var id: String? = null,
        @SerializedName("user_id") var userId: String? = null,
        @SerializedName("event_id") var eventId: Long? = null,
        @SerializedName("event_code") var eventCode: String? = null,
        @SerializedName("event_user") var eventUser: String? = null,
        @SerializedName("activity_info") var activityInfoList: List<WorkoutInfoForRank>? = null,
        @SerializedName("total_distance") var totalDistance: Double? = null,
        @SerializedName("rank_no") var rankNo: Int? = null,
        @SerializedName("user_info") var userInfo: UserInfo? = null)