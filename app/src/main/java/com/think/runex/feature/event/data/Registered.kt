package com.think.runex.feature.event.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Registered(
        @SerializedName("owner_id") var ownerId: String? = null,
        @SerializedName("user_code") var userCode: String? = null,
        @SerializedName("event_code") var eventCode: String? = null,
        @SerializedName("ref2") var ref2: String? = null,
        @SerializedName("regs") var registeredDataList: List<RegisteredData>? = null,
        @SerializedName("event") var eventDetail: EventDetail? = null) : Parcelable {

    var isChecked: Boolean = false

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(RegisteredData)) {
        isChecked = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ownerId)
        parcel.writeString(userCode)
        parcel.writeString(eventCode)
        parcel.writeString(ref2)
        parcel.writeTypedList(registeredDataList)
        parcel.writeByte(if (isChecked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Registered> {
        override fun createFromParcel(parcel: Parcel): Registered {
            return Registered(parcel)
        }

        override fun newArray(size: Int): Array<Registered?> {
            return arrayOfNulls(size)
        }
    }

    fun getEventName(): String = eventDetail?.title ?: ""

    @JvmName("getEventCodeJava")
    fun getEventCode(): String = eventCode ?: eventDetail?.code ?: ""


    fun getRegisterStatus(position: Int): String? {
        if (position < registeredDataList?.size ?: 0) {
            return registeredDataList?.get(0)?.status
        }
        return null
    }

    fun isRegisterSuccess(position: Int): Boolean {
        if (position < registeredDataList?.size ?: 0) {
            return registeredDataList?.get(0)?.status == RegisterStatus.SUCCESS
        }
        return false
    }

    fun getOrderId(position: Int): String {
        if (position < registeredDataList?.size ?: 0) {
            return registeredDataList?.get(0)?.orderId ?: ""
        }
        return ""
    }

    fun getRegisterId(position: Int): String {
        if (position < registeredDataList?.size ?: 0) {
            return registeredDataList?.get(0)?.id ?: ""
        }
        return ""
    }

    fun getTotalPrice(): Double {
        if (registeredDataList?.isNotEmpty() == true) {
            return registeredDataList?.get(0)?.getTotalPrice() ?: 0.0
        }
        return 0.0
    }

    fun getTicketAtRegister(): Ticket? {
        if (registeredDataList?.isNotEmpty() == true) {
            return registeredDataList?.get(0)?.ticketOptions?.get(0)?.ticket
        }
        return null
    }

    fun getParentRegisterId(): String {
        var id = ""
        if (registeredDataList?.isNotEmpty() == true) {
            id = registeredDataList?.get(0)?.parentRegisterId ?: ""
        }
        if (id.contains("0000000000000")) {
            id = ""
        }
        return id
    }

    fun getTeamLeaderRegisteredData(): RegisteredData? {
        return registeredDataList?.firstOrNull { it.isTeamLead == true }
    }

    fun getTeamLeaderUserId(): String {
        return registeredDataList?.firstOrNull { it.isTeamLead == true }?.userId ?: ""
    }
}

