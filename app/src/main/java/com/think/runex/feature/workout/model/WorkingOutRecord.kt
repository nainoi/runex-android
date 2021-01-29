package com.think.runex.feature.workout.model

import android.os.Parcel
import android.os.Parcelable
import com.jozzee.android.core.text.toDoubleOrZero
import com.think.runex.common.displayFormat
import com.think.runex.common.timeDisplayFormat
import java.util.concurrent.TimeUnit

data class WorkingOutRecord(

        /**
         * Type of workout such as running, walking, clicking
         */
        var type: String,

        /**
         * Start working out time in millisecond.
         */
        var start: Long = 0,

        /**
         * Stop working out time in millisecond.
         */
        var stop: Long = 0,

        /**
         * Time durations on working out in millisecond.
         */
        var duration: Long = 0,

        /**
         * Distances on working out in meters
         */
        var distances: Float = 0f) : Parcelable {

    companion object CREATOR : Parcelable.Creator<WorkingOutRecord> {
        override fun createFromParcel(parcel: Parcel): WorkingOutRecord {
            return WorkingOutRecord(parcel)
        }

        override fun newArray(size: Int): Array<WorkingOutRecord?> {
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
        parcel.writeLong(start)
        parcel.writeLong(stop)
        parcel.writeLong(duration)
        parcel.writeFloat(distances)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getDistancesKilometers(): Float = (distances / 1000f)

    fun getDurationSecond(): Long = duration / 1000

    fun getDurationMinutePerKilometer(): Double {
        val secPerKilometer: Long = ((duration / distances)).toLong()
        val minutes = TimeUnit.SECONDS.toMinutes(secPerKilometer)
        val second = TimeUnit.SECONDS.toSeconds(secPerKilometer) - TimeUnit.MINUTES.toSeconds(minutes)
        return ("$minutes.$second").toDoubleOrZero()
    }


    //TODO("Unreliable calculation!")
    fun getCalories(): Double {
        if (distances > 0) {
            //TODO("don't know the reference, from old java code")
            val caloriesBurnPerHour = 450.0

            val hour = TimeUnit.MILLISECONDS.toHours(duration)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(hour)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes)

            val percentageOfMinutes: Float = (minutes * 100) / 60f
            val percentageOfSeconds: Float = (seconds * 100) / 3600f

            val caloriesFromHour = caloriesBurnPerHour * hour
            val caloriesFromMinute = (percentageOfMinutes * caloriesBurnPerHour) / 100
            val caloriesFromSecond = (percentageOfSeconds * caloriesBurnPerHour) / 100

            return (caloriesFromHour + caloriesFromMinute + caloriesFromSecond)
        }
        return 0.0
    }

    fun getDisplayData(): WorkingOutDisplayData {

        val displayData = WorkingOutDisplayData()

        //Update distance
        displayData.distances = getDistancesKilometers().displayFormat(awaysShowDecimal = true)

        //Update duration
        displayData.duration = duration.timeDisplayFormat()

        //Update duration per kilometer
        if (distances > 0f) {
            val millisPerKilometer: Long = ((duration / distances) * 1000).toLong()
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
        displayData.calories = getCalories().displayFormat()

        return displayData
    }
}