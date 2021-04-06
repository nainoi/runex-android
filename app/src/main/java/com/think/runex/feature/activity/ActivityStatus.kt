package com.think.runex.feature.activity

import android.content.Context
import android.graphics.drawable.GradientDrawable
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.think.runex.R

object ActivityStatus {
    const val APPROVE = "APPROVE"
    const val WAITING = "WAITING"
    const val FAILED = "FAILED"


    @ColorRes
    fun getStatusColor(status: String?) = when (status) {
        APPROVE -> R.color.statusSuccess
        WAITING -> R.color.statusWaiting
        FAILED -> R.color.error
        else -> R.color.statusUnknown
    }

    fun getStatusBackground(context: Context, status: String?) = GradientDrawable().apply {
        shape = GradientDrawable.OVAL
        cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        setColor(ContextCompat.getColor(context, getStatusColor(status)))
    }


    fun getStatusText(context: Context, status: String): String = when (status) {
        APPROVE -> context.getString(R.string.approve)
        WAITING -> context.getString(R.string.waiting_for_approve)
        FAILED -> context.getString(R.string.failed)
        else -> ""
    }
}