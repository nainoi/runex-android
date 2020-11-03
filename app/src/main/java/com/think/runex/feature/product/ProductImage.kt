package com.think.runex.feature.product

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ProductImage(@SerializedName("path_url") var url: String = "") : Parcelable {

    companion object CREATOR : Parcelable.Creator<ProductImage> {
        override fun createFromParcel(parcel: Parcel): ProductImage {
            return ProductImage(parcel)
        }

        override fun newArray(size: Int): Array<ProductImage?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }
}