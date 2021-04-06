package com.think.runex.feature.dashboard.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class DashboardInfoRequestBody(
        @SerializedName("event_code") var eventCode: String = "",
        @SerializedName("order_id") var orderId: String = "",
        @SerializedName("reg_id") var registerId: String = "",
        @SerializedName("parent_reg_id") var parentRegisterId: String = "") : Parcelable {

    companion object CREATOR : Parcelable.Creator<DashboardInfoRequestBody> {
        override fun createFromParcel(parcel: Parcel): DashboardInfoRequestBody {
            return DashboardInfoRequestBody(parcel)
        }

        override fun newArray(size: Int): Array<DashboardInfoRequestBody?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(eventCode)
        parcel.writeString(orderId)
        parcel.writeString(registerId)
        parcel.writeString(parentRegisterId)
    }

    override fun describeContents(): Int {
        return 0
    }
}