package com.think.runex.feature.event.data

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.jozzee.android.core.datetime.dateTimeFormat
import com.think.runex.R
import com.think.runex.config.DISPLAY_DATE_FORMAT_SHOT_MONTH
import com.think.runex.config.SERVER_DATE_TIME_FORMAT
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.event.data.request.TicketOptionEventRegistrationBody

data class EventRegisteredData(
        @SerializedName("id") var id: String? = null,
        @SerializedName("user_id") var userId: String? = null,
        @SerializedName("event_id") var eventId: String? = null,
        @SerializedName("event_code") var eventCode: String? = null,
        @SerializedName("status") var status: String? = null,
        @SerializedName("payment_type") var paymentType: String? = null,
        @SerializedName("total_price") var totalPrice: Double? = 0.0,
        @SerializedName("discount_price") var discountPrice: Double? = 0.0,
        @SerializedName("promo_code") var promoCode: String? = null,
        @SerializedName("order_id") var orderId: String? = null,
        @SerializedName("reg_date") var registerDate: String? = null,
        @SerializedName("payment_date") var paymentPate: String? = null,
        @SerializedName("coupon") var coupon: Coupon? = null,
        @SerializedName("ticket_options") var ticketOptions: List<TicketOptionEventRegistrationBody>? = null,
        @SerializedName("partner") var partner: Partner? = null,
        @SerializedName("event") var event: EventDetail? = null,
        @SerializedName("created_at") var createdAt: String? = null,
        @SerializedName("updated_at") var updatedAt: String? = null) : Parcelable {

    companion object CREATOR : Parcelable.Creator<EventRegisteredData> {
        override fun createFromParcel(parcel: Parcel): EventRegisteredData {
            return EventRegisteredData(parcel)
        }

        override fun newArray(size: Int): Array<EventRegisteredData?> {
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
            parcel.readValue(Float::class.java.classLoader) as? Double,
            parcel.readValue(Float::class.java.classLoader) as? Double,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Coupon::class.java.classLoader),
            parcel.createTypedArrayList(TicketOptionEventRegistrationBody),
            parcel.readParcelable(Partner::class.java.classLoader),
            parcel.readParcelable(EventDetail::class.java.classLoader),
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
        parcel.writeString(registerDate)
        parcel.writeString(paymentPate)
        parcel.writeParcelable(coupon, flags)
        parcel.writeTypedList(ticketOptions)
        parcel.writeParcelable(partner, flags)
        parcel.writeParcelable(event, flags)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun registerEventPeriod(context: Context): String {
        val startEventDate = event?.eventStartDate?.dateTimeFormat(SERVER_DATE_TIME_FORMAT, DISPLAY_DATE_FORMAT_SHOT_MONTH)
                ?: ""
        val endEventDate = event?.eventEndDate?.dateTimeFormat(SERVER_DATE_TIME_FORMAT, DISPLAY_DATE_FORMAT_SHOT_MONTH)
                ?: ""
        return "${context.getString(R.string.register_date)} $startEventDate - $endEventDate"
    }

    @JvmName("getCoverImageJava")
    fun getCoverImage(): String {
        return when (event?.coverImage?.startsWith("http", false) == true) {
            true -> event?.coverImage ?: ""
            false -> ("${ApiConfig.BASE_URL}${event?.coverImage ?: ""}")
        }
    }

    fun getTotalPrice(): Double {
        if ((totalPrice ?: 0.0) > (discountPrice ?: 0.0)) {
            return (totalPrice ?: 0.0) - (discountPrice ?: 0.0)
        }
        return 0.0
    }
}