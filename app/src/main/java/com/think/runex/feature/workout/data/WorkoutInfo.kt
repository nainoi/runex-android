package com.think.runex.feature.workout.data

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.annotation.DrawableRes
import com.google.gson.annotations.SerializedName
import com.jozzee.android.core.datetime.dateTimeFormat
import com.jozzee.android.core.datetime.toTimeMillis
import com.think.runex.R
import com.think.runex.config.DISPLAY_DATE_FORMAT
import com.think.runex.util.extension.displayFormat
import com.think.runex.util.extension.timeDisplayFormat
import com.think.runex.config.DISPLAY_DATE_TIME_FORMAT_THREE_LETTERS_DATE_MONTH
import com.think.runex.config.SERVER_DATE_TIME_FORMAT
import com.think.runex.config.SERVER_DATE_TIME_FORMAT_2
import java.text.SimpleDateFormat
import java.util.*

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
    @SerializedName("workout_date") var workoutDate: String? = null,
    @SerializedName("loc_url") var locationsUrl: String? = null
) : Parcelable {

    companion object CREATOR : Parcelable.Creator<WorkoutInfo> {
        override fun createFromParcel(parcel: Parcel): WorkoutInfo {
            return WorkoutInfo(parcel)
        }

        override fun newArray(size: Int): Array<WorkoutInfo?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.createTypedArrayList(WorkingOutLocation),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

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
        timeDisplay = record?.duration?.timeDisplayFormat() ?: ""
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(activityType)
        parcel.writeString(app)
        parcel.writeValue(calories)
        parcel.writeString(caption)
        parcel.writeValue(distanceKilometers)
        parcel.writeValue(durationSecond)
        parcel.writeString(endDate)
        parcel.writeString(id)
        parcel.writeValue(isSync)
        parcel.writeValue(netElevationGain)
        parcel.writeTypedList(locations)
        parcel.writeValue(durationMinutePerKilometer)
        parcel.writeString(refId)
        parcel.writeString(startDate)
        parcel.writeString(timeDisplay)
        parcel.writeString(workoutDate)
        parcel.writeString(locationsUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getDisplayData(): WorkingOutDisplayData {

        val displayData = WorkingOutDisplayData()

        //Update distance
        displayData.distances = distanceKilometers?.displayFormat(awaysShowDecimal = true) ?: "0.00"

        //Update duration
        displayData.duration = timeDisplay
            ?: "00:00:00"//((durationSecond ?: 0L) * 1000).timeDisplayFormat()

        //Update duration per kilometer
        if (distanceKilometers ?: 0f > 0) {
            val millisPerKilometer: Long = (((durationSecond ?: 0) / (distanceKilometers
                ?: 0f)) * 1000).toLong()
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

    fun getWorkoutDateTime(displayPattern: String = DISPLAY_DATE_TIME_FORMAT_THREE_LETTERS_DATE_MONTH): String {
        var text = ""
        try {
            val sdf = SimpleDateFormat(SERVER_DATE_TIME_FORMAT, Locale.getDefault())
            sdf.timeZone = TimeZone.getDefault()
            sdf.parse(workoutDate ?: "")?.also { date ->
                text = SimpleDateFormat(displayPattern, Locale.getDefault()).format(date)
            }
        } catch (e: Throwable) {
            try {
                val sdf = SimpleDateFormat(SERVER_DATE_TIME_FORMAT_2, Locale.getDefault())
                sdf.timeZone = TimeZone.getDefault()
                sdf.parse(workoutDate ?: "")?.also { date ->
                    text = SimpleDateFormat(displayPattern, Locale.getDefault()).format(date)
                }
            } catch (e: Throwable) {
                try {
                    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    sdf.timeZone = TimeZone.getDefault()
                    sdf.parse(workoutDate ?: "")?.also { date ->
                        text = SimpleDateFormat(displayPattern, Locale.getDefault()).format(date)
                    }
                } catch (e: Throwable) {
                }
            }
        }
        return text
    }

    fun getWorkoutDateTimeMillis(): Long {
        var time: Long = 0
        try {
            time = SimpleDateFormat(SERVER_DATE_TIME_FORMAT, Locale.getDefault())
                .parse(workoutDate ?: "")?.time ?: 0
        } catch (e: Throwable) {
            try {
                time = SimpleDateFormat(SERVER_DATE_TIME_FORMAT_2, Locale.getDefault())
                    .parse(workoutDate ?: "")?.time ?: 0
            } catch (e: Throwable) {
                try {
                    time = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                        .parse(workoutDate ?: "")?.time ?: 0
                } catch (e: Throwable) {

                }
            }
        }
        return time
    }

    fun getWorkoutDateTimeForImageName(): String {
        return workoutDate?.dateTimeFormat(SERVER_DATE_TIME_FORMAT, "yyyyMMddHHmm") ?: ""
    }

    fun getDistances(context: Context): String {
        return "${distanceKilometers?.displayFormat(awaysShowDecimal = true) ?: ""} ${context.getString(R.string.km)}"
    }

    @DrawableRes
    fun getPartnerIcon(): Int = when {
        app?.contains(Partner.GARMIN, true) == true -> R.mipmap.ic_logo_garmin
        app?.contains(Partner.SUUNTO, true) == true -> R.mipmap.ic_logo_suunto
        app?.contains(Partner.STRAVA, true) == true -> R.mipmap.ic_logo_strava
        app?.contains(Partner.FITBIT, true) == true -> R.mipmap.ic_logo_fitbit
        else -> R.drawable.ic_running
    }
}