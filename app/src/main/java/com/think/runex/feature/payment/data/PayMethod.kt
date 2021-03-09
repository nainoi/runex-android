package com.think.runex.feature.payment.data

import com.google.gson.annotations.SerializedName

data class PayMethod(
        @SerializedName("id") var id: String? = null,
        @SerializedName("name") var name: String? = null,
        @SerializedName("charge") var charge: Int? = 0,
        @SerializedName("charge_percent") var chargePercent: Float? = 0f,
        @SerializedName("is_active") var isActive: Boolean? = false,
        @SerializedName("icon") var icon: String? = null)