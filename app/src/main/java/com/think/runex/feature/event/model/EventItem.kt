package com.think.runex.feature.event.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.think.runex.datasource.api.ApiConfig

data class EventItem(
        @SerializedName("code") var code: String? = "",
        @SerializedName("content") var content: String? = "",
        @SerializedName("cover") var coverImage: String? = null,
        @SerializedName("event_date") var eventDateDisplay: String? = null,
        @SerializedName("event_start_date") var eventStartDate: String? = null,
        @SerializedName("event_end_date") var eventEndDate: String? = null,
        @SerializedName("title") var title: String? = "") : Parcelable {

    companion object CREATOR : Parcelable.Creator<EventItem> {
        override fun createFromParcel(parcel: Parcel): EventItem {
            return EventItem(parcel)
        }

        override fun newArray(size: Int): Array<EventItem?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(code)
        parcel.writeString(content)
        parcel.writeString(coverImage)
        parcel.writeString(eventDateDisplay)
        parcel.writeString(eventStartDate)
        parcel.writeString(eventEndDate)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun coverImage(): String {
        return when (coverImage?.startsWith("http", false) == true) {
            true -> coverImage ?: ""
            false -> ("${ApiConfig.BASE_URL}${coverImage ?: ""}")
        }
    }

    //    fun eventPeriod(context: Context): String {
//        val startEventDate = eventStartDate?.dateTimeFormat(SERVER_DATE_TIME_FORMAT, DISPLAY_DATE_FORMAT_SHOT_MONTH)
//                ?: ""
//        val endEventDate = eventEndDate?.dateTimeFormat(SERVER_DATE_TIME_FORMAT, DISPLAY_DATE_FORMAT_SHOT_MONTH)
//                ?: ""
//        return "${context.getString(R.string.event_date)} $startEventDate - $endEventDate"
//    }
//
}