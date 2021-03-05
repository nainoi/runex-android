package com.think.runex.feature.workout.data

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.think.runex.R
import com.think.runex.common.displayFormat

data class WorkoutHistoryDay(
        @SerializedName("id") var id: String? = "",
        /**
         * Distance in Kilometers
         */
        @SerializedName("distance") var distances: Double? = 0.0,
        /**
         * Date time of start working out (in display format: yyyy-MM-dd HH:mm:ss)
         */
        @SerializedName("workout_date") var workoutDate: String? = null,
        /**
         * Duration display format ("00:00:00")
         */
        @SerializedName("workout_time") var workoutTime: String? = null) {

    fun getWorkoutDateTime(): String = "${workoutDate ?: ""}\n${workoutTime ?: ""}"

    fun getDistances(context: Context): String {
        return "${distances?.displayFormat(awaysShowDecimal = true) ?: ""} ${context.getString(R.string.km)}"
    }
}