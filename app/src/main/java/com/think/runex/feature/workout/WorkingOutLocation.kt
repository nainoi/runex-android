package com.think.runex.feature.workout

import android.location.Location
import android.os.Parcel
import android.os.Parcelable
import com.google.android.libraries.maps.model.LatLng
import com.think.runex.java.Utils.L
import io.realm.RealmObject

open class WorkingOutLocation(
        var startMillis: Long = 0,
        var timeMillis: Long = 0,
        var latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var accuracy: Float = 0f,
        var altitude: Double = 0.0) : RealmObject(), Parcelable {

    companion object CREATOR : Parcelable.Creator<WorkingOutLocation> {
        override fun createFromParcel(parcel: Parcel): WorkingOutLocation {
            return WorkingOutLocation(parcel)
        }

        override fun newArray(size: Int): Array<WorkingOutLocation?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readLong(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readFloat(),
            parcel.readDouble())

    constructor(startMillis: Long?, location: Location?) : this(
            startMillis ?: 0,
            location?.time ?: 0,
            location?.latitude ?: 0.0,
            location?.longitude ?: 0.0,
            location?.accuracy ?: 0f,
            location?.altitude ?: 0.0)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(startMillis)
        parcel.writeLong(timeMillis)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeFloat(accuracy)
        parcel.writeDouble(altitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun toLatLng() = LatLng(latitude, longitude)

    fun copy(location: Location?) {
        timeMillis = location?.time ?: 0
        latitude = location?.latitude ?: 0.0
        longitude = location?.longitude ?: 0.0
        accuracy = location?.accuracy ?: 0f
        altitude = location?.altitude ?: 0.0
    }
}