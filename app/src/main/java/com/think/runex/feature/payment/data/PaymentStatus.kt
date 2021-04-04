package com.think.runex.feature.payment.data

import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.think.runex.R

object PaymentStatus {
    const val WAITING_CONFIRM = "WAIT_CONFIRM"
    const val WAITING_APPROVE = "PAYMENT_WAITING_APPROVE"
    const val WAITING_PAY = "PAYMENT_WAITING"
    const val SUCCESS = "PAYMENT_SUCCESS"
    const val FAILED = "PAYMENT_FAIL"

    @ColorRes
    fun getPaymentStatusColor(paymentState: String?) = when (paymentState) {
        WAITING_APPROVE -> R.color.statusWaitingForApprove
        WAITING_PAY -> R.color.statusWaiting
        SUCCESS -> R.color.statusSuccess
        FAILED -> R.color.error
        else -> R.color.textColorHint
    }

    fun getPaymentStatusBackground(context: Context, paymentState: String?) = GradientDrawable().apply {
        shape = GradientDrawable.OVAL
        cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        setColor(ContextCompat.getColor(context, getPaymentStatusColor(paymentState)))
    }


    fun getPaymentStatusText(context: Context, paymentState: String): String = when (paymentState) {
        WAITING_APPROVE -> context.getString(R.string.waiting_for_approve)
        WAITING_PAY -> context.getString(R.string.waiting_for_payment)
        SUCCESS -> context.getString(R.string.success)
        FAILED -> context.getString(R.string.failed)
        else -> ""
    }
}