package com.think.runex.feature.event.model

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.jozzee.android.core.datetime.dateTimeFormat
import com.think.runex.R
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.ticket.Ticket
import com.think.runex.config.DISPLAY_DATE_FORMAT_SHOT_MONTH
import com.think.runex.config.SERVER_DATE_TIME_FORMAT

data class Event(
        @SerializedName("id") var id: String = "",
        @SerializedName("name") var name: String = "",
        @SerializedName("description") var description: String? = null,
        @SerializedName("body") var body: String? = null,
        @SerializedName("cover") var coverImage: String? = null,
        @SerializedName("cover_thumb") var coverThumbnailImages: List<EventCoverThumbnailImage>? = null,
        @SerializedName("category") var category: String? = null,
        @SerializedName("slug") var slug: String? = null,
        @SerializedName("ticket") var ticket: List<Ticket>? = null,
        @SerializedName("owner_id") var ownerId: String? = null,
        @SerializedName("status") var status: String? = null,
        @SerializedName("location") var location: String? = null,
        @SerializedName("receive_location") var receiveLocation: String? = null,
        @SerializedName("is_active") var isActive: Boolean = false,
        @SerializedName("is_free") var isFree: Boolean = false,
        @SerializedName("start_reg") var startRegisterDate: String = "",
        @SerializedName("end_reg") var endRegisterDate: String = "",
        @SerializedName("start_event") var startEventDate: String = "",
        @SerializedName("end_event") var endEventDate: String = "",
        @SerializedName("inapp") var isInApp: Boolean = false,
        @SerializedName("is_post") var isPost: Boolean = false,
        @SerializedName("post_end_date") var postEndDate: String? = null,
        @SerializedName("partner") var partner: Partner? = null,
        @SerializedName("created_time") var createdTime: String? = null,
        @SerializedName("updated_time") var updatedTime: String? = null) : Parcelable {

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(EventCoverThumbnailImage),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(Ticket),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readParcelable(Partner::class.java.classLoader),
            parcel.readString(),
            parcel.readString())


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(body)
        parcel.writeString(coverImage)
        parcel.writeTypedList(coverThumbnailImages)
        parcel.writeString(category)
        parcel.writeString(slug)
        parcel.writeTypedList(ticket)
        parcel.writeString(ownerId)
        parcel.writeString(status)
        parcel.writeString(location)
        parcel.writeString(receiveLocation)
        parcel.writeByte(if (isActive) 1 else 0)
        parcel.writeByte(if (isFree) 1 else 0)
        parcel.writeString(startRegisterDate)
        parcel.writeString(endRegisterDate)
        parcel.writeString(startEventDate)
        parcel.writeString(endEventDate)
        parcel.writeByte(if (isInApp) 1 else 0)
        parcel.writeByte(if (isPost) 1 else 0)
        parcel.writeString(postEndDate)
        parcel.writeParcelable(partner, flags)
        parcel.writeString(createdTime)
        parcel.writeString(updatedTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun coverImage(): String = when (coverImage?.isNotBlank() == true) {
        true -> when (coverImage?.startsWith("http") == true) {
            true -> coverImage ?: ""
            false -> ("${ApiConfig.BASE_URL}$coverImage")
        }
        false -> ""
    }


    fun eventPeriod(context: Context): String {
        return "${context.getString(R.string.event_date)} " +
                "${startEventDate.dateTimeFormat(SERVER_DATE_TIME_FORMAT, DISPLAY_DATE_FORMAT_SHOT_MONTH)} - " +
                endEventDate.dateTimeFormat(SERVER_DATE_TIME_FORMAT, DISPLAY_DATE_FORMAT_SHOT_MONTH)
    }

    fun registerPeriod(context: Context): String {
        return "${context.getString(R.string.register_date)} " +
                "${startRegisterDate.dateTimeFormat(SERVER_DATE_TIME_FORMAT, DISPLAY_DATE_FORMAT_SHOT_MONTH)} - " +
                endRegisterDate.dateTimeFormat(SERVER_DATE_TIME_FORMAT, DISPLAY_DATE_FORMAT_SHOT_MONTH)
    }
}