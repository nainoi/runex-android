package com.think.runex.feature.event.model.old.registered

import com.google.gson.annotations.SerializedName
import com.think.runex.feature.user.UserInfo

data class TicketOptionsRegisteredEvent(
        @SerializedName("user_option") var userOption: UserInfo? = null,
        @SerializedName("total_price") var totalPrice: Float? = 0f,
        @SerializedName("register_number") var registerNumber: String? = null,
        @SerializedName("reciept_type") var receiptType: String? = null,
        @SerializedName("tickets") var tickets: List<TicketRegisteredEvent>? = null)