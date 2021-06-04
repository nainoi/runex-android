package com.think.runex.feature.dashboard

import com.think.runex.feature.dashboard.data.DeleteActivityBody

interface OnDeleteActivityListener {

    fun onDeleteActivity(
        userPosition: Int,
        activityPosition: Int,
        activityId: String,
        activityToDelete: DeleteActivityBody,
    )
}