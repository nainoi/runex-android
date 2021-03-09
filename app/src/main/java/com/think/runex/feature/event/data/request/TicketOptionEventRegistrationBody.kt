package com.think.runex.feature.event.data.request

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.think.runex.feature.event.data.Shirt
import com.think.runex.feature.event.data.Ticket

data class TicketOptionEventRegistrationBody(
        @SerializedName("user_option") var userOption: UserOptionEventRegistrationBody? = null,
        @SerializedName("tickets") var ticket: Ticket? = null,
        @SerializedName("shirts") var shirt: Shirt? = null,
        @SerializedName("total_price") var totalPrice: Double = 0.0,
        @SerializedName("reciept_type") var receiptType: String = "") : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(UserOptionEventRegistrationBody::class.java.classLoader),
            parcel.readParcelable(Ticket::class.java.classLoader),
            parcel.readParcelable(Shirt::class.java.classLoader),
            parcel.readDouble(),
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(userOption, flags)
        parcel.writeParcelable(ticket, flags)
        parcel.writeParcelable(shirt, flags)
        parcel.writeDouble(totalPrice)
        parcel.writeString(receiptType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TicketOptionEventRegistrationBody> {
        override fun createFromParcel(parcel: Parcel): TicketOptionEventRegistrationBody {
            return TicketOptionEventRegistrationBody(parcel)
        }

        override fun newArray(size: Int): Array<TicketOptionEventRegistrationBody?> {
            return arrayOfNulls(size)
        }
    }

}