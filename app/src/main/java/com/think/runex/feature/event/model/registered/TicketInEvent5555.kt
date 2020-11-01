package com.think.runex.feature.event.model.registered

import com.google.gson.annotations.SerializedName

data class TicketInEvent5555(
        @SerializedName("ticket_id") var id: String = "",
        @SerializedName("ticket_name") var title: String? = "",
        @SerializedName("distance") var distance: Float? = 0f,
        @SerializedName("total_price") var totalPrice: Double? = 0.0,
        @SerializedName("type") var type: String? = null,
        @SerializedName("remark") var remark: String? = null)
        //@SerializedName("product") var product: Any? = null)