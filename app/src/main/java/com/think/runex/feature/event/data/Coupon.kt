package com.think.runex.feature.event.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Coupon(
        @SerializedName("id") var id: String? = "",
        @SerializedName("sale_id") var saleId: String? = "",
        @SerializedName("discount") var discount: Float? = 0f,
        @SerializedName("coupon_code") var couponCode: String? = "",
        @SerializedName("description") var description: String? = "",
        @SerializedName("start_date") var startDate: String? = "",
        @SerializedName("end_date") var endDate: String? = "",
        @SerializedName("active") var isActive: Boolean? = false,
        @SerializedName("created_at") var createdAt: String? = "",
        @SerializedName("updated_at") var updatedAt: String? = "") : Parcelable {

    companion object CREATOR : Parcelable.Creator<Coupon> {
        override fun createFromParcel(parcel: Parcel): Coupon {
            return Coupon(parcel)
        }

        override fun newArray(size: Int): Array<Coupon?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Float::class.java.classLoader) as? Float,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(saleId)
        parcel.writeValue(discount)
        parcel.writeString(couponCode)
        parcel.writeString(description)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeValue(isActive)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }
}