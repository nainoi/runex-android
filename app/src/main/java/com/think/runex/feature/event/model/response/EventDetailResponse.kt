package com.think.runex.feature.event.model.response

import com.google.gson.annotations.SerializedName
import com.think.runex.feature.event.model.EventDetail
import com.think.runex.feature.event.model.TicketEventDetail

data class EventDetailResponse(
        @SerializedName("event") var event: EventDetail? = null,
        @SerializedName("tickets") var tickets: List<TicketEventDetail>? = null)