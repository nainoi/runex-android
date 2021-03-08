package com.think.runex.feature.event.data.request

import com.google.gson.annotations.SerializedName
import com.think.runex.feature.event.data.Shirt
import com.think.runex.feature.event.data.Ticket

data class TicketEventRegistrationBody(
        @SerializedName("user_option") var userData: UserEventRegistrationBody? = null,
        @SerializedName("tickets") var ticket: Ticket? = null,
        @SerializedName("shirts") var shirt: Shirt? = null,
        @SerializedName("total_price") var totalPrice: Double = 0.0,
        @SerializedName("reciept_type") var receiptType: String = "")