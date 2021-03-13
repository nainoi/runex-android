package com.think.runex.feature.payment.data

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.google.gson.annotations.SerializedName
import com.think.runex.R
import com.think.runex.common.displayFormat

data class PaymentMethod(
        @SerializedName("id") var id: String? = null,
        @SerializedName("name") var name: String? = null,
        @SerializedName("charge") var charge: Float? = 0f,
        @SerializedName("charge_percent") var chargePercent: Double? = 0.0,
        @SerializedName("is_active") var isActive: Boolean? = false,
        @SerializedName("icon") var icon: String? = null) {


    fun getPaymentMethodIcon(context: Context): Drawable? = when (icon) {
        "credit_card" -> ContextCompat.getDrawable(context, R.drawable.ic_credit_card)
        "qr_code" -> ContextCompat.getDrawable(context, R.drawable.ic_qr_code)
        else -> null
    }

    fun getChargeAmount(amount: Double): Double {
        return (amount * (chargePercent ?: 0.0)) / 100.0
    }

    fun getChargeAmountDisplay(context: Context, amount: Double): String {
        return "+${getChargeAmount(amount).displayFormat(awaysShowDecimal = true)} ${context.getString(R.string.thai_bath)}"
    }

}