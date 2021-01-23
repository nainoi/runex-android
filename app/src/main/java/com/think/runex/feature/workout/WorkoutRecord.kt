package com.think.runex.feature.workout

import com.think.runex.common.displayFormat
import com.think.runex.common.timeDisplayFormat

data class WorkoutRecord(

        /**
         * Type of workout such as running, walking, clicking
         */
        var type: String,

        /**
         * Start working out time in millisecond.
         */
        var startMillis: Long = 0,

        /**
         * Stop working out time in millisecond.
         */
        var stopMillis: Long = 0,

        /**
         * Time durations on working out in millisecond.
         */
        var durationMillis: Long = 0,

        /**
         * Distances on working out in meters
         */
        var distance: Float = 0f) {

    fun getDisplayData() = WorkingOutDisplayData().apply {
        duration = durationMillis.timeDisplayFormat()
        distance = (this@WorkoutRecord.distance / 1000f).displayFormat(awaysShowDecimal = true)
    }
}