package com.think.runex.feature.activity

import com.google.gson.annotations.SerializedName

data class AddActivityRequest(
        @SerializedName("image") var imageUri: String,
        @SerializedName("distance") var distance: Float,
        @SerializedName("activity_date") var date: String,
        @SerializedName("event_id") var eventId: String,
        @SerializedName("activity_type") var type: String)