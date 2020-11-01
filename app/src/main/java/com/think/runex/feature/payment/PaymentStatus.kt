package com.think.runex.feature.payment

import android.content.Context
import androidx.annotation.ColorRes
import com.think.runex.R

object PaymentStatus {
    const val WAITING_APPROVE = "PAYMENT_WAITING_APPROVE"
    const val WAITING = "PAYMENT_WAITING"
    const val SUCCESS = "PAYMENT_SUCCESS"
    const val FAIL = "PAYMENT_FAIL"

    @ColorRes
    fun getPaymentStatusColor(paymentState: String) = when (paymentState) {
        WAITING_APPROVE -> R.color.paymentStatusWaitingForApprove
        WAITING -> R.color.paymentStatusWaiting
        SUCCESS -> R.color.paymentStatusSuccess
        FAIL -> R.color.error
        else -> R.color.textColorHint
    }

    fun getPaymentStatusText(context: Context, paymentState: String): String = when (paymentState) {
        WAITING_APPROVE -> context.getString(R.string.waiting_for_approve)
        WAITING -> context.getString(R.string.waiting_for_payment)
        SUCCESS -> context.getString(R.string.success)
        FAIL -> context.getString(R.string.failed)
        else -> ""
    }
}