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
        var distances: Float = 0f) {

    fun getDistancesKilometers(): String = (distances / 1000f).displayFormat(awaysShowDecimal = true)


    fun getDisplayData(): WorkingOutDisplayData {

        val displayData = WorkingOutDisplayData()
        displayData.duration = durationMillis.timeDisplayFormat()
        displayData.distances = getDistancesKilometers()

        if (distances > 0f) {
            val millisPerKilometer: Long = ((durationMillis / distances) * 1000).toLong()
            //Check durations millisecond per kilometer more than 1 hour (3600000 millisecond)
            when (millisPerKilometer < 3600000) {
                true -> displayData.durationPerKilometer = millisPerKilometer.timeDisplayFormat().let {
                    if (it.length >= 3) {
                        return@let it.substring(3, it.length)
                    }
                    return@let it
                }
                false -> {
                    displayData.durationPerKilometer = millisPerKilometer.timeDisplayFormat()
                    displayData.durationPerKilometerUnit = "(hr/km)"
                }
            }
        }

        return displayData
    }
}