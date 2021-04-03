package com.think.runex.feature.activity.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.jozzee.android.core.datetime.dateTimeFormat
import com.think.runex.util.extension.displayFormat
import com.think.runex.config.DISPLAY_DATE_TIME_FORMAT_THREE_LETTERS_DATE_MONTH
import com.think.runex.config.SERVER_DATE_TIME_FORMAT

data class ActivityInfo(
        @SerializedName("id") var id: String? = "",
        @SerializedName("distance") var distance: Double? = 0.0,
        @SerializedName("img_url") var imgUrl: String? = "",
        @SerializedName("caption") var caption: String? = "",
        @SerializedName("app") var app: String? = "",
        @SerializedName("time") var time: Long? = 0,
        @SerializedName("is_approve") var isApprove: Boolean? = false,
        @SerializedName("status") var status: String? = "",
        @SerializedName("activity_date") var activityDate: String? = "",
        @SerializedName("created_at") var createdAt: String? = "",
        @SerializedName("updated_at") var updatedAt: String? = "") : Parcelable {

    companion object CREATOR : Parcelable.Creator<ActivityInfo> {
        override fun createFromParcel(parcel: Parcel): ActivityInfo {
            return ActivityInfo(parcel)
        }

        override fun newArray(size: Int): Array<ActivityInfo?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Long::class.java.classLoader) as? Long,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeValue(distance)
        parcel.writeString(imgUrl)
        parcel.writeString(caption)
        parcel.writeString(app)
        parcel.writeValue(time)
        parcel.writeValue(isApprove)
        parcel.writeString(status)
        parcel.writeString(activityDate)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getDistanceDisplay(unit: String): String = "${(distance ?: 0.0).displayFormat()} $unit"

    fun getActivityDateDisplay(): String {
        return "${activityDate?.dateTimeFormat(SERVER_DATE_TIME_FORMAT, DISPLAY_DATE_TIME_FORMAT_THREE_LETTERS_DATE_MONTH)}"
    }
}