package com.think.runex.feature.event.data

import com.google.gson.annotations.SerializedName

data class EventRegisteredForSubmitResult(
        @SerializedName("event_code") var eventCode: String = "",
        @SerializedName("event_id") var eventId: String = "",
        @SerializedName("title") var eventName: String = "",
        @SerializedName("partner") var partner: Partner? = null)
