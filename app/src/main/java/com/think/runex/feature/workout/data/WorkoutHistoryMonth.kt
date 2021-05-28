package com.think.runex.feature.workout.data

import com.google.gson.annotations.SerializedName
import com.jozzee.android.core.datetime.toTimeMillis
import com.think.runex.config.SERVER_DATE_TIME_FORMAT
import com.think.runex.config.SERVER_DATE_TIME_FORMAT_2
import java.text.ParseException


data class WorkoutHistoryMonth(
    @SerializedName("year") var year: Int? = 0,
    @SerializedName("month") var month: Int? = 0,
    @SerializedName("month_name") var monthName: String? = null,

    /**
     * Distance in Kilometers
     */
    @SerializedName("total_distance") var totalDistances: Double? = 0.0,

    /**
     * Duration display format ("00:00:00")
     */
    @SerializedName("time_string") var totalDuration: String? = null,

    /**
     * Calories in kilo calorie.
     */
    @SerializedName("calory") var calories: Double? = 0.0,

    /**
     * Workout info list each a month
     */
    @SerializedName("workout_day") var workouts: List<WorkoutInfo>? = null
) {

    fun getMontAndYear() = "$monthName $year"

}