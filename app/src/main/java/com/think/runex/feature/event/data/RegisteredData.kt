package com.think.runex.feature.event.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class RegisteredData(
        @SerializedName("id") var id: String? = "",
        @SerializedName("user_id") var userId: String? = "",
        @SerializedName("event_id") var eventId: String? = "",
        @SerializedName("event_code") var eventCode: String? = "",
        @SerializedName("status") var status: String? = "",
        @SerializedName("payment_type") var paymentType: String? = "",
        @SerializedName("total_price") var totalPrice: Double? = 0.0,
        @SerializedName("discount_price") var discountPrice: Double? = 0.0,
        @SerializedName("promo_code") var promoCode: String? = "",
        @SerializedName("order_id") var orderId: String? = "",
        @SerializedName("is_team_lead") var isTeamLead: Boolean? = false,
        @SerializedName("reg_date") var registerDate: String? = "",
        @SerializedName("payment_date") var paymentDate: String? = "",
        @SerializedName("coupon") var coupon: Coupon? = Coupon(),
        @SerializedName("ticket_options") var ticketOptions: List<TicketOptionEventRegistration>? = null,
        @SerializedName("partner") var partner: Partner? = Partner(),
        @SerializedName("parent_reg_id") var parentRegisterId: String? = "",
        @SerializedName("created_at") var createdAt: String? = "",
        @SerializedName("updated_at") var updatedAt: String? = "") : Parcelable {

    companion object CREATOR : Parcelable.Creator<RegisteredData> {
        override fun createFromParcel(parcel: Parcel): RegisteredData {
            return RegisteredData(parcel)
        }

        override fun newArray(size: Int): Array<RegisteredData?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Coupon::class.java.classLoader),
            parcel.createTypedArrayList(TicketOptionEventRegistration),
            parcel.readParcelable(Partner::class.java.classLoader),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(userId)
        parcel.writeString(eventId)
        parcel.writeString(eventCode)
        parcel.writeString(status)
        parcel.writeString(paymentType)
        parcel.writeValue(totalPrice)
        parcel.writeValue(discountPrice)
        parcel.writeString(promoCode)
        parcel.writeString(orderId)
        parcel.writeValue(isTeamLead)
        parcel.writeString(registerDate)
        parcel.writeString(paymentDate)
        parcel.writeParcelable(coupon, flags)
        parcel.writeTypedList(ticketOptions)
        parcel.writeParcelable(partner, flags)
        parcel.writeString(parentRegisterId)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
    }

    fun getTotalPrice(): Double {
        if ((totalPrice ?: 0.0) > (discountPrice ?: 0.0)) {
            return (totalPrice ?: 0.0) - (discountPrice ?: 0.0)
        }
        return 0.0
    }

    override fun describeContents(): Int {
        return 0
    }
}