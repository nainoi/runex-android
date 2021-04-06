package com.think.runex.feature.activity.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.think.runex.feature.event.data.Ticket

data class ActivityForSubmitEvent(
        @SerializedName("activity_info") var activityInfo: ActivityInfo? = null,
        @SerializedName("event_code") var eventCode: String? = "",
        @SerializedName("id") var id: String? = "",
        @SerializedName("order_id") var orderId: String? = "",
        @SerializedName("parent_reg_id") var parentRegisterId: String? = "",
        @SerializedName("reg_id") var registerId: String? = "",
        @SerializedName("ticket") var ticket: Ticket? = Ticket(),
        @SerializedName("user_id") var userId: String? = "") : Parcelable {

    companion object CREATOR : Parcelable.Creator<ActivityForSubmitEvent> {
        override fun createFromParcel(parcel: Parcel): ActivityForSubmitEvent {
            return ActivityForSubmitEvent(parcel)
        }

        override fun newArray(size: Int): Array<ActivityForSubmitEvent?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readParcelable(ActivityInfo::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Ticket::class.java.classLoader),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(activityInfo, flags)
        parcel.writeString(eventCode)
        parcel.writeString(id)
        parcel.writeString(orderId)
        parcel.writeString(parentRegisterId)
        parcel.writeString(registerId)
        parcel.writeParcelable(ticket, flags)
        parcel.writeString(userId)
    }

    override fun describeContents(): Int {
        return 0
    }
}