package com.think.runex.feature.product

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ProductType(
        @SerializedName("name") var name: String = "",
        @SerializedName("remark") var remark: String = "",
        @SerializedName("price") var price: Float = 0f) : Parcelable {

    companion object CREATOR : Parcelable.Creator<ProductType> {
        override fun createFromParcel(parcel: Parcel): ProductType {
            return ProductType(parcel)
        }

        override fun newArray(size: Int): Array<ProductType?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readFloat())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(remark)
        parcel.writeFloat(price)
    }

    override fun describeContents(): Int {
        return 0
    }
}