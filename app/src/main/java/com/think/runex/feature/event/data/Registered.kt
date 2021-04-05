package com.think.runex.feature.event.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Registered(
        @SerializedName("owner_id") var ownerId: String? = null,
        @SerializedName("user_code") var userCode: String? = null,
        @SerializedName("event_code") var eventCode: String? = null,
        @SerializedName("ref2") var ref2: String? = null,
        @SerializedName("regs") var registerDataList: List<RegisterData>? = null,
        @SerializedName("event") var eventDetail: EventDetail? = null) : Parcelable {

    var isChecked: Boolean = false

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(RegisterData)) {
        isChecked = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ownerId)
        parcel.writeString(userCode)
        parcel.writeString(eventCode)
        parcel.writeString(ref2)
        parcel.writeTypedList(registerDataList)
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
        if (position < registerDataList?.size ?: 0) {
            return registerDataList?.get(0)?.status
        }
        return null
    }

    fun isRegisterSuccess(position: Int): Boolean {
        if (position < registerDataList?.size ?: 0) {
            return registerDataList?.get(0)?.status == RegisterStatus.SUCCESS
        }
        return false
    }

    fun getOrderId(position: Int): String {
        if (position < registerDataList?.size ?: 0) {
            return registerDataList?.get(0)?.orderId ?: ""
        }
        return ""
    }

    fun getRegisterId(position: Int): String {
        if (position < registerDataList?.size ?: 0) {
            return registerDataList?.get(0)?.id ?: ""
        }
        return ""
    }

    fun getTotalPrice(): Double {
        if (registerDataList?.isNotEmpty() == true) {
            return registerDataList?.get(0)?.getTotalPrice() ?: 0.0
        }
        return 0.0
    }

    fun getTicketAtRegister(): Ticket? {
        if (registerDataList?.isNotEmpty() == true) {
            return registerDataList?.get(0)?.ticketOptions?.get(0)?.ticket
        }
        return null
    }

    fun getParentRegisterId(): String {
        var id = ""
        if (registerDataList?.isNotEmpty() == true) {
            id = registerDataList?.get(0)?.id ?: ""
        }
        if (id.contains("0000000000000")) {
            id = ""
        }
        return id
    }

    fun getParentRegisterData(): RegisterData? {
        return registerDataList?.firstOrNull { it.isTeamLead == true }
    }

    fun getTeamLeaderUserId(): String {
        return registerDataList?.firstOrNull { it.isTeamLead == true }?.userId ?: ""
    }
}

