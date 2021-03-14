package com.think.runex.feature.payment.data.request

import com.google.gson.annotations.SerializedName

data class PayEventBody(
        @SerializedName("token") var omiseTokenId: String = "",
        @SerializedName("price") var price: Double = 0.0,
        @SerializedName("event_code") var eventCode: String = "",
        @SerializedName("reg_id") var registerId: String = "",
        @SerializedName("order_id") var orderId: String = "")