package com.think.runex.feature.event.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.think.runex.feature.payment.data.PaymentStatus

data class EventRegistered(
        @SerializedName("owner_id") var ownerId: String? = null,
        @SerializedName("user_code") var userCode: String? = null,
        @SerializedName("event_code") var eventCode: String? = null,
        @SerializedName("ref2") var ref2: String? = null,
        @SerializedName("regs") var eventRegisteredList: List<EventRegisteredData>? = null) : Parcelable {

    var isChecked: Boolean = false

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(EventRegisteredData)) {
        isChecked = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ownerId)
        parcel.writeString(userCode)
        parcel.writeString(eventCode)
        parcel.writeString(ref2)
        parcel.writeTypedList(eventRegisteredList)
        parcel.writeByte(if (isChecked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EventRegistered> {
        override fun createFromParcel(parcel: Parcel): EventRegistered {
            return EventRegistered(parcel)
        }

        override fun newArray(size: Int): Array<EventRegistered?> {
            return arrayOfNulls(size)
        }
    }

    fun getEventName(): String {
        if (eventRegisteredList?.isNotEmpty() == true) {
            return eventRegisteredList?.get(0)?.event?.title ?: ""
        }
        return ""
    }


    @JvmName("getEventCodeJava")
    fun getEventCode(): String {
        if (eventRegisteredList?.isNotEmpty() == true) {
            return eventRegisteredList?.get(0)?.eventCode ?: ""
        }
        return ""
    }

    fun getEventId(): String {
        if (eventRegisteredList?.isNotEmpty() == true) {
            return (eventRegisteredList?.get(0)?.event?.id ?: 0).toString()
        }
        return ""
    }

    fun getEventDetail(): EventDetail? {
        if (eventRegisteredList?.isNotEmpty() == true) {
            return eventRegisteredList?.get(0)?.event
        }
        return null
    }

    fun getPaymentStatus(): String? {
        if (eventRegisteredList?.isNotEmpty() == true) {
            return eventRegisteredList?.get(0)?.status
        }
        return null
    }

    fun isPaid(): Boolean {
        if (eventRegisteredList?.isNotEmpty() == true) {
            return eventRegisteredList?.get(0)?.status == PaymentStatus.SUCCESS
        }
        return false
    }

    fun getTotalPrice(): Double {
        if (eventRegisteredList?.isNotEmpty() == true) {
            return eventRegisteredList?.get(0)?.getTotalPrice() ?: 0.0
        }
        return 0.0
    }

    fun getOrderId(): String {
        if (eventRegisteredList?.isNotEmpty() == true) {
            return eventRegisteredList?.get(0)?.orderId ?: ""
        }
        return ""
    }

    fun getRegisterId(): String {
        if (eventRegisteredList?.isNotEmpty() == true) {
            return eventRegisteredList?.get(0)?.id ?: ""
        }
        return ""
    }


    fun getPartner(): Partner? {
        if (eventRegisteredList?.isNotEmpty() == true) {
            return eventRegisteredList?.get(0)?.partner
        }
        return null
    }
}

