package com.think.runex.feature.workout.data

import android.content.res.Resources
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.think.runex.R

data class WorkingOutDisplayData(
        /**
         * Distance from start to last or stop in kilometers.
         */
        @SerializedName("distances") var distances: String = "0.00",

        /**
         * Time durations from start to last or stop.
         */
        @SerializedName("duration") var duration: String = "00:00:00",

        /**
         * Time duration in minutes or hour per kilometer.
         * Format 00:00 (min/km) or 00:00:00 (hr/km)
         */
        @SerializedName("durationPerKilometer") var durationPerKilometer: String = "00:00",

        /**
         * The unit of time duration per kilometer
         * (min/km) or (hr/km)
         */
        @SerializedName("durationPerKilometerUnit") var durationPerKilometerUnit: String = "(min/km)",

        /**
         * calories in working out
         */
        @SerializedName("calories") var calories: String = "0.00") : Parcelable {

    companion object CREATOR : Parcelable.Creator<WorkingOutDisplayData> {
        override fun createFromParcel(parcel: Parcel): WorkingOutDisplayData {
            return WorkingOutDisplayData(parcel)
        }

        override fun newArray(size: Int): Array<WorkingOutDisplayData?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(distances)
        parcel.writeString(duration)
        parcel.writeString(durationPerKilometer)
        parcel.writeString(durationPerKilometerUnit)
        parcel.writeString(calories)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getNotificationContent(resources: Resources): String {
        return "$duration, $distances ${resources.getString(R.string.km)}"
    }
}