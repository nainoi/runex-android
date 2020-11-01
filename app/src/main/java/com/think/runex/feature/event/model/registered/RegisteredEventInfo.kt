package com.think.runex.feature.event.model.registered

import com.google.gson.annotations.SerializedName
import com.think.runex.feature.event.model.Event
import com.think.runex.feature.event.model.Partner

data class RegisteredEventInfo(
        @SerializedName("id") var id: String = "",
        @SerializedName("user_id") var userId: String = "",
        @SerializedName("status") var paymentStatus: String = "",
        @SerializedName("payment_type") var paymentType: String = "",
        @SerializedName("total_price") var totalPrice: Float = 0f,
        @SerializedName("discount_price") var discountPrice: Float = 0f,
        @SerializedName("promo_code") var promotionCode: String? = null,
        @SerializedName("order_id") var orderId: String? = null,
        @SerializedName("reg_date") var registerDate: String = "",
        @SerializedName("payment_date") var paymentDate: String = "",
        @SerializedName("register_number") var registerNumber: String = "",
        @SerializedName("coupon") var coupon: Coupon? = null,
        @SerializedName("ticket_options") var ticketOptions: TicketOptions? = null,
        @SerializedName("partner") var partner: Partner? = null,
        @SerializedName("event") var event: Event? = null,
        @SerializedName("created_at") var createdAt: String? = null,
        @SerializedName("updated_at") var updatedAt: String? = null)