package com.think.runex.feature.workout.data

import android.location.Location
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.jozzee.android.core.datetime.dateTimeFormat
import com.think.runex.config.SERVER_DATE_TIME_FORMAT
import com.think.runex.feature.workout.data.WorkingOutLocation.CREATOR.TABLE_NAME

@Entity(tableName = TABLE_NAME)
open class WorkingOutLocation(
    /**
     * Date tine string format [SERVER_DATE_TIME_FORMAT]
     */
    @PrimaryKey
    @ColumnInfo(name = TIME)
    @SerializedName(TIME)
    var timeServerFormat: String = "",

    @ColumnInfo(name = "latitude")
    @SerializedName("latitude")
    var latitude: Double? = 0.0,

    @ColumnInfo(name = "longitude")
    @SerializedName("longitude")
    var longitude: Double? = 0.0,

    @ColumnInfo(name = "accuracy")
    @SerializedName("accuracy")
    var accuracy: Float? = 0f,

    @ColumnInfo(name = "altitude")
    @SerializedName("altitude")
    var altitude: Double? = 0.0,

    @ColumnInfo(name = "elevation_gain")
    @SerializedName("elevation_gain")
    var elevationGain: Double? = 0.0,

    @ColumnInfo(name = "harth_rate")
    @SerializedName("harth_rate")
    var heartRate: Double? = 0.0,

    @ColumnInfo(name = "temp")
    @SerializedName("temp")
    var temp: Double? = 0.0

) : Parcelable {

    companion object CREATOR : Parcelable.Creator<WorkingOutLocation> {

        const val TABLE_NAME = "workout_locations"
        const val TIME = "timestamp"

        override fun createFromParcel(parcel: Parcel): WorkingOutLocation {
            return WorkingOutLocation(parcel)
        }

        override fun newArray(size: Int): Array<WorkingOutLocation?> {
            return arrayOfNulls(size)
        }
    }

    constructor() : this(
        "",
        0.0,
        0.0,
        0f,
        0.0,
        0.0,
        0.0,
        0.0
    )

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double
    )

    constructor(location: Location?) : this(
        location?.time?.dateTimeFormat(SERVER_DATE_TIME_FORMAT) ?: "",
        location?.latitude ?: 0.0,
        location?.longitude ?: 0.0,
        location?.accuracy ?: 0f,
        location?.altitude ?: 0.0
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(timeServerFormat)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
        parcel.writeValue(accuracy)
        parcel.writeValue(altitude)
        parcel.writeValue(elevationGain)
        parcel.writeValue(heartRate)
        parcel.writeValue(temp)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun toLatLng() = LatLng(latitude ?: 0.0, longitude ?: 0.0)

    fun copy(location: Location?) {
        timeServerFormat = location?.time?.dateTimeFormat(SERVER_DATE_TIME_FORMAT) ?: ""
        latitude = location?.latitude ?: 0.0
        longitude = location?.longitude ?: 0.0
        accuracy = location?.accuracy ?: 0f
        altitude = location?.altitude ?: 0.0
    }
}