package com.think.runex.feature.event.model.registered

import com.google.gson.annotations.SerializedName

data class RegisteredEvent(
        @SerializedName("event_id") var eventId: String = "",
        @SerializedName("regs") var registerInfoList: List<RegisteredEventInfo>? = null)