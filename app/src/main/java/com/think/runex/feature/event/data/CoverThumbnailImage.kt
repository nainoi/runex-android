package com.think.runex.feature.event.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CoverThumbnailImage(
        @SerializedName("image") var image: String? = null,
        @SerializedName("size") var size: String? = null) : Parcelable {

    companion object CREATOR : Parcelable.Creator<CoverThumbnailImage> {
        override fun createFromParcel(parcel: Parcel): CoverThumbnailImage {
            return CoverThumbnailImage(parcel)
        }

        override fun newArray(size: Int): Array<CoverThumbnailImage?> {
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