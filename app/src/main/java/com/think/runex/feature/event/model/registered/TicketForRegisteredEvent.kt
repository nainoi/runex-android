package com.think.runex.feature.event.model.registered

import com.google.gson.annotations.SerializedName

data class TicketForRegisteredEvent(
        @SerializedName("ticket_id") var id: String? = "",
        @SerializedName("ticket_name") var name: String? = "",
        @SerializedName("distance") var distance: Float? = 0f,
        @SerializedName("total_price") var totalPrice: Float? = 0f,
        @SerializedName("type") var type: String? = "",
        @SerializedName("remark") var remark: String? = "",
        @SerializedName("product") var product: Any? = null)