package com.think.runex.feature.event.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Partner(
        @SerializedName("partner_id") var partnerId: String = "",
        @SerializedName("partner_name") var partnerName: String = "",
        @SerializedName("slug") var slug: String = "",
        @SerializedName("ref_event_key") var refEventKey: String = "",
        @SerializedName("ref_activity_key") var refActivityKey: String = "") : Parcelable {

    companion object CREATOR : Parcelable.Creator<Partner> {
        override fun createFromParcel(parcel: Parcel): Partner {
            return Partner(parcel)
        }

        override fun newArray(size: Int): Array<Partner?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(partnerId)
        parcel.writeString(partnerName)
        parcel.writeString(slug)
        parcel.writeString(refEventKey)
        parcel.writeString(refActivityKey)
    }

    override fun describeContents(): Int {
        return 0
    }
}