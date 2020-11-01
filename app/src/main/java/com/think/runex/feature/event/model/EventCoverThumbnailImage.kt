package com.think.runex.feature.event.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class EventCoverThumbnailImage(
        @SerializedName("image") var image: String? = null,
        @SerializedName("size") var size: String? = null) : Parcelable {

    companion object CREATOR : Parcelable.Creator<EventCoverThumbnailImage> {
        override fun createFromParcel(parcel: Parcel): EventCoverThumbnailImage {
            return EventCoverThumbnailImage(parcel)
        }

        override fun newArray(size: Int): Array<EventCoverThumbnailImage?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(image)
        parcel.writeString(size)
    }

    override fun describeContents(): Int {
        return 0
    }
}