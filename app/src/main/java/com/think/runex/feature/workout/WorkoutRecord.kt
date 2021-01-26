package com.think.runex.feature.workout

import android.os.Parcel
import android.os.Parcelable
import com.think.runex.common.displayFormat
import com.think.runex.common.timeDisplayFormat
import java.util.concurrent.TimeUnit

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
        var distances: Float = 0f) : Parcelable {

    companion object CREATOR : Parcelable.Creator<WorkoutRecord> {
        override fun createFromParcel(parcel: Parcel): WorkoutRecord {
            return WorkoutRecord(parcel)
        }

        override fun newArray(size: Int): Array<WorkoutRecord?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readLong(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.readFloat())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeLong(startMillis)
        parcel.writeLong(stopMillis)
        parcel.writeLong(durationMillis)
        parcel.writeFloat(distances)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getDistancesKilometers(): String = (distances / 1000f).displayFormat(awaysShowDecimal = true)


    fun getDisplayData(): WorkingOutDisplayData {

        val displayData = WorkingOutDisplayData()

        //Update distance
        displayData.distances = getDistancesKilometers()

        //Update duration
        displayData.duration = durationMillis.timeDisplayFormat()

        //Update duration per kilometer
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

        //TODO("Unreliable calculation!")
        //Update calories
        if (distances > 0) {
            //TODO("don't know the reference, from old java code")
            val caloriesBurnPerHour = 450.0

            val hour = TimeUnit.MILLISECONDS.toHours(durationMillis)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) - TimeUnit.HOURS.toMinutes(hour)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis) - TimeUnit.MINUTES.toSeconds(minutes)

            val percentageOfMinutes: Float = (minutes * 100) / 60f
            val percentageOfSeconds: Float = (seconds * 100) / 3600f

            val caloriesFromHour = caloriesBurnPerHour * hour
            val caloriesFromMinute = (percentageOfMinutes * caloriesBurnPerHour) / 100
            val caloriesFromSecond = (percentageOfSeconds * caloriesBurnPerHour) / 100

            displayData.calories = (caloriesFromHour + caloriesFromMinute + caloriesFromSecond).displayFormat()
        } else {
            displayData.calories = "0"
        }

        return displayData
    }
}