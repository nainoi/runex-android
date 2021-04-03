package com.think.runex.feature.workout.data

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.jozzee.android.core.datetime.dateTimeFormat
import com.think.runex.R
import com.think.runex.util.extension.displayFormat
import com.think.runex.config.DISPLAY_DATE_FORMAT
import com.think.runex.config.SERVER_DATE_TIME_FORMAT

data class WorkoutHistoryDay(
        @SerializedName("id") var id: String? = "",
        /**
         * Distance in Kilometers
         */
        @SerializedName("distance") var distances: Double? = 0.0,
        /**
         * Date time of start working out (in server format: yyyy-MM-dd'T'HH:mm:ssZ)
         */
        @SerializedName("workout_date") var workoutDate: String? = null,
        /**
         * Duration display format ("00:00:00")
         */
        @SerializedName("time_string") var workoutTime: String? = null) {

    fun getWorkoutDateTime(): String {
        return "${workoutDate?.dateTimeFormat(SERVER_DATE_TIME_FORMAT, DISPLAY_DATE_FORMAT)}\n${workoutTime ?: ""}"
    }

    fun getDistances(context: Context): String {
        return "${distances?.displayFormat(awaysShowDecimal = true) ?: ""} ${context.getString(R.string.km)}"
    }
}