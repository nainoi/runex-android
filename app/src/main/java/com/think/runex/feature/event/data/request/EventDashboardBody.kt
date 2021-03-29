package com.think.runex.feature.event.data.request

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class EventDashboardBody(
        @SerializedName("event_code") var eventCode: String = "",
        @SerializedName("order_id") var orderId: String = "",
        @SerializedName("parent_reg_id") var parentRegId: String = "",
        @SerializedName("reg_id") var regId: String = "") : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(eventCode)
        parcel.writeString(orderId)
        parcel.writeString(parentRegId)
        parcel.writeString(regId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EventDashboardBody> {
        override fun createFromParcel(parcel: Parcel): EventDashboardBody {
            return EventDashboardBody(parcel)
        }

        override fun newArray(size: Int): Array<EventDashboardBody?> {
            return arrayOfNulls(size)
        }
    }
}