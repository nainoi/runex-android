package com.think.runex.feature.workout

import android.content.res.Resources
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.think.runex.R

data class WorkingOutDisplayData(
        /**
         * Time durations from start to last or stop
         */
        @SerializedName("duration") var duration: String = "00:00:00",

        /**
         * Distance from start to last or stop in kilometers
         */
        @SerializedName("distance") var distance: String = "0.00",

        /**
         *
         */
        @SerializedName("distancePerHour") var distancePerHour: String = "0.00",

        /**
         *
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
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(duration)
        parcel.writeString(distance)
        parcel.writeString(distancePerHour)
        parcel.writeString(calories)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun notificationContent(resources: Resources): String {
        return "$duration, $distance ${resources.getString(R.string.km)}"
    }
}