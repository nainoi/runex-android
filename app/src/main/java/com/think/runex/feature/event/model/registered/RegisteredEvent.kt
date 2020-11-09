package com.think.runex.feature.event.model.registered

import com.google.gson.annotations.SerializedName

data class RegisteredEvent(
        @SerializedName("event_id") var eventId: String = "",
        @SerializedName("regs") var registerInfoList: List<RegisteredEventInfo>? = null) {

    var isChecked: Boolean = false

    fun getEventInfo(): RegisteredEventInfo? {
        if (registerInfoList?.isNotEmpty() == true) {
            return registerInfoList?.get(0)
        }
        return null
    }
}