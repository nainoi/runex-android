package com.think.runex.feature.event.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TicketEventDetail(
        @SerializedName("category") var category: String? = "",
        @SerializedName("created_at") var createdAt: String? = "",
        @SerializedName("detail") var detail: String? = "",
        @SerializedName("distance") var distance: String? = "",
        @SerializedName("event_id") var eventId: String? = "",
        @SerializedName("id") var id: String? = "",
        @SerializedName("items") var items: Any? = null,
        @SerializedName("limit") var limit: String? = "",
        @SerializedName("photo_map") var photoMap: String? = null,
        @SerializedName("photo_medal") var photoMedal: String? = null,
        @SerializedName("photo_shirt") var photoShirt: String? = null,
        @SerializedName("price") var price: String? = null,
        @SerializedName("title") var title: String? = null,
        @SerializedName("updated_at") var updatedAt: String? = null) : Parcelable {

    companion object CREATOR : Parcelable.Creator<TicketEventDetail> {
        override fun createFromParcel(parcel: Parcel): TicketEventDetail {
            return TicketEventDetail(parcel)
        }

        override fun newArray(size: Int): Array<TicketEventDetail?> {
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
            null,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(category)
        parcel.writeString(createdAt)
        parcel.writeString(detail)
        parcel.writeString(distance)
        parcel.writeString(eventId)
        parcel.writeString(id)
        parcel.writeString(limit)
        parcel.writeString(photoMap)
        parcel.writeString(photoMedal)
        parcel.writeString(photoShirt)
        parcel.writeString(price)
        parcel.writeString(title)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getTitle(unit: String): String {
        return "$title $distance $unit"
    }

    @JvmName("getPriceDisplay")
    fun getPrice(): String {
        return "$price THB"
    }
}