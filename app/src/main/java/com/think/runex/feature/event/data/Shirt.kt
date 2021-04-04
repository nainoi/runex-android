package com.think.runex.feature.event.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Shirt(
        @SerializedName("chest") var chest: String? = "",
        @SerializedName("id") var id: Int? = 0,
        @SerializedName("length") var length: String? = "",
        @SerializedName("short_sleeve_shirt") var shortSleeveShirt: Boolean? = false,
        @SerializedName("size") var size: String? = "",
        @SerializedName("sleeveless_shirt") var sleevelessShirt: Boolean? = false) : Parcelable {

    companion object CREATOR : Parcelable.Creator<Shirt> {
        override fun createFromParcel(parcel: Parcel): Shirt {
            return Shirt(parcel)
        }

        override fun newArray(size: Int): Array<Shirt?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(chest)
        parcel.writeValue(id)
        parcel.writeString(length)
        parcel.writeValue(shortSleeveShirt)
        parcel.writeString(size)
        parcel.writeValue(sleevelessShirt)
    }

    override fun describeContents(): Int {
        return 0
    }
}