package com.think.runex.feature.product

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Product(
        @SerializedName("id") var id: String? = "",
        @SerializedName("name") var name: String? = "",
        @SerializedName("image") var images: List<ProductImage>? = null,
        @SerializedName("detail") var detail: String? = "",
        @SerializedName("status") var status: String? = "",
        @SerializedName("reuse") var isReuse: Boolean? = false,
        @SerializedName("is_show") var isShow: Boolean? = false,
        @SerializedName("sizes") var sizes: ProductSize? = null,
        @SerializedName("created_at") var createdAt: String? = "",
        @SerializedName("updated_at") var updatedAt: String? = "") : Parcelable {

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(ProductImage),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readParcelable(ProductSize::class.java.classLoader),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeTypedList(images)
        parcel.writeString(detail)
        parcel.writeString(status)
        parcel.writeByte(if (isReuse == true) 1 else 0)
        parcel.writeByte(if (isShow == true) 1 else 0)
        parcel.writeParcelable(sizes, flags)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }
}