package com.think.runex.feature.event.data

import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.think.runex.R

object RegisterStatus {
    const val REGISTER = "REGISTER"
    const val WAITING_CONFIRM = "WAIT_CONFIRM"
    const val WAITING_APPROVE = "PAYMENT_WAITING_APPROVE"
    const val WAITING_PAY = "PAYMENT_WAITING"
    const val SUCCESS = "PAYMENT_SUCCESS"
    const val FAILED = "PAYMENT_FAIL"

    @ColorRes
    fun getPaymentStatusColor(paymentState: String?, isClosed: Boolean): Int {
        if (isClosed) return R.color.statusWaiting
        return when (paymentState) {
            WAITING_APPROVE -> R.color.statusUnknown
            WAITING_PAY -> R.color.statusWaiting
            SUCCESS -> R.color.statusSuccess
            FAILED -> R.color.error
            else -> R.color.statusUnknown
        }
    }

    fun getPaymentStatusBackground(context: Context, paymentState: String?, isClosed: Boolean) = GradientDrawable().apply {
        shape = GradientDrawable.OVAL
        cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        setColor(ContextCompat.getColor(context, getPaymentStatusColor(paymentState, isClosed)))
    }


    fun getPaymentStatusText(context: Context, paymentState: String, isClosed: Boolean): String {
        if (isClosed) return context.getString(R.string.closed)
        return when (paymentState) {
            WAITING_APPROVE -> context.getString(R.string.waiting_for_approve)
            WAITING_PAY -> context.getString(R.string.waiting_for_payment)
            WAITING_CONFIRM -> context.getString(R.string.waiting_for_confirm)
            SUCCESS -> context.getString(R.string.success)
            FAILED -> context.getString(R.string.failed)
            else -> ""
        }
    }
}