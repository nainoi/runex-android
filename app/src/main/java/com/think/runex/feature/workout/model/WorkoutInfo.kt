package com.think.runex.feature.workout.model

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.jozzee.android.core.datetime.dateTimeFormat
import com.think.runex.common.displayFormat
import com.think.runex.common.timeDisplayFormat
import com.think.runex.config.SERVER_DATE_TIME_FORMAT

data class WorkoutInfo(
        @SerializedName("activity_type") var activityType: String? = null,
        @SerializedName("app") var app: String? = null,
        @SerializedName("calory") var calories: Double? = 0.0,
        @SerializedName("caption") var caption: String? = null,
        @SerializedName("distance") var distanceKilometers: Float? = 0f,
        @SerializedName("duration") var durationSecond: Long? = 0,
        @SerializedName("end_date") var endDate: String? = null,
        @SerializedName("id") var id: String? = null,
        @SerializedName("is_sync") var isSync: Boolean? = false,
        @SerializedName("net_elevation_gain") var netElevationGain: Double? = 0.0,
        @SerializedName("locations") var locations: List<WorkingOutLocation>? = null,
        @SerializedName("pace") var durationMinutePerKilometer: Double? = 0.0,
        @SerializedName("ref_id") var refId: String? = null,
        @SerializedName("start_date") var startDate: String? = null,
        @SerializedName("time_string") var timeDisplay: String? = null,
        @SerializedName("workout_date") var workoutDate: String? = null) {

    constructor(record: WorkingOutRecord?, locations: List<WorkingOutLocation>?) : this(
            activityType = record?.type ?: WorkoutType.RUNNING,
            app = "RUNEX",
            calories = record?.getCalories() ?: 0.0,
            caption = "",
            distanceKilometers = record?.getDistancesKilometers() ?: 0f,
            durationSecond = record?.getDurationSecond() ?: 0,
            endDate = record?.stop?.dateTimeFormat(SERVER_DATE_TIME_FORMAT) ?: "",
            id = "",
            isSync = false,
            netElevationGain = 0.0,
            locations = locations ?: emptyList(),
            durationMinutePerKilometer = record?.getDurationMinutePerKilometer() ?: 0.0,
            refId = "",
            startDate = record?.start?.dateTimeFormat(SERVER_DATE_TIME_FORMAT) ?: "",
            workoutDate = record?.start?.dateTimeFormat(SERVER_DATE_TIME_FORMAT) ?: "",
            timeDisplay = record?.duration?.timeDisplayFormat() ?: "")


    fun getDisplayData(): WorkingOutDisplayData {

        val displayData = WorkingOutDisplayData()

        //Update distance
        displayData.distances = distanceKilometers?.displayFormat(awaysShowDecimal = true) ?: "0.00"

        //Update duration
        displayData.duration = timeDisplay ?: "00:00:00"//((durationSecond ?: 0L) * 1000).timeDisplayFormat()

        //Update duration per kilometer
        if (distanceKilometers ?: 0f > 0) {
            val millisPerKilometer: Long = (((durationSecond ?: 0) / (distanceKilometers ?: 0f)) * 1000).toLong()
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

        //Update calories
        displayData.calories = calories?.displayFormat() ?: "0"

        return displayData
    }
}