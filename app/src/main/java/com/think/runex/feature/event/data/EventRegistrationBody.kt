package com.think.runex.feature.event.data

import com.google.gson.annotations.SerializedName
import com.think.runex.feature.event.data.EventDetail
import com.think.runex.feature.event.data.TicketOptionEventRegistration
import com.think.runex.feature.payment.data.PaymentStatus

data class EventRegistrationBody(
        @SerializedName("event") var event: EventDetail? = null,
        @SerializedName("status") var status: String = PaymentStatus.WAITING_PAY,
        @SerializedName("payment_type") var paymentType: String = "",
        @SerializedName("total_price") var totalPrice: Double = 0.0,
        @SerializedName("discount_price") var discountPrice: Double = 0.0,
        @SerializedName("promo_code") var promotionCode: String = "",
        @SerializedName("reg_date") var registerDate: String = "",
        @SerializedName("ticket_id") var ticketId: String = "",
        @SerializedName("ticket_options") var ticketOptions: List<TicketOptionEventRegistration>? = null)