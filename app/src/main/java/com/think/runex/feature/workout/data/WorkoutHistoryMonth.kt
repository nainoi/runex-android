package com.think.runex.feature.workout.data

import com.google.gson.annotations.SerializedName


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
        @SerializedName("workout_day") var workouts: List<WorkoutHistoryDay>? = null) {

    fun getMontAndYear() = "$monthName $year"
}