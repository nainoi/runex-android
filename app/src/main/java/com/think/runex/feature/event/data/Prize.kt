package com.think.runex.feature.event.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Prize(
        @SerializedName("description") var description: String? = null,
        @SerializedName("id") var id: Int? = 0,
        @SerializedName("name") var name: String? = null,
        @SerializedName("photo") var photo: String? = null) : Parcelable {

    companion object CREATOR : Parcelable.Creator<Prize> {
        override fun createFromParcel(parcel: Parcel): Prize {
            return Prize(parcel)
        }

        override fun newArray(size: Int): Array<Prize?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(photo)
    }

    override fun describeContents(): Int {
        return 0
    }
}