package com.think.runex.feature.event.model.old.registered

import com.google.gson.annotations.SerializedName

data class Coupon(
        @SerializedName("id") var id: String? = "",
        @SerializedName("sale_id") var saleId: String? = "",
        @SerializedName("discount") var discount: Float? = 0f,
        @SerializedName("coupon_code") var couponCode: String? = null,
        @SerializedName("description") var description: String? = null,
        @SerializedName("start_date") var startDate: String? = "",
        @SerializedName("end_date") var endDate: String? = "",
        @SerializedName("active") var isActive: Boolean? = false,
        @SerializedName("created_at") var createdAt: String? = null,
        @SerializedName("updated_at") var updatedAt: String? = null)