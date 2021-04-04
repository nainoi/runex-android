package com.think.runex.feature.event.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TicketOptionEventRegistration(
        @SerializedName("user_option") var userOption: UserOptionEventRegistration? = null,
        @SerializedName("tickets") var ticket: Ticket? = null,
        @SerializedName("shirts") var shirt: Shirt? = null,
        @SerializedName("total_price") var totalPrice: Double = 0.0,
        @SerializedName("reciept_type") var receiptType: String = "") : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(UserOptionEventRegistration::class.java.classLoader),
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

    companion object CREATOR : Parcelable.Creator<TicketOptionEventRegistration> {
        override fun createFromParcel(parcel: Parcel): TicketOptionEventRegistration {
            return TicketOptionEventRegistration(parcel)
        }

        override fun newArray(size: Int): Array<TicketOptionEventRegistration?> {
            return arrayOfNulls(size)
        }
    }

}