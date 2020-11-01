package com.think.runex.feature.product

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ProductSize(
        @SerializedName("name") var name: String = "",
        @SerializedName("remark") var remark: String = "") : Parcelable {

    companion object CREATOR : Parcelable.Creator<ProductSize> {
        override fun createFromParcel(parcel: Parcel): ProductSize {
            return ProductSize(parcel)
        }

        override fun newArray(size: Int): Array<ProductSize?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(remark)
    }

    override fun describeContents(): Int {
        return 0
    }

}