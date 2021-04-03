package com.think.runex.feature.event.data

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.think.runex.R
import com.think.runex.util.extension.numberDisplayFormat

data class Ticket(
        @SerializedName("category") var category: String? = null,
        @SerializedName("created_at") var createdAt: String? = null,
        @SerializedName("detail") var detail: String? = null,
        @SerializedName("distance") var distance: String? = null,
        @SerializedName("event_id") var eventId: String? = null,
        @SerializedName("id") var id: String? = null,
        @SerializedName("items") var items: Any? = null,
        @SerializedName("limit") var limit: String? = null,
        @SerializedName("photo_map") var photoMap: String? = null,
        @SerializedName("photo_medal") var photoMedal: String? = null,
        @SerializedName("photo_shirt") var photoShirt: String? = null,
        @SerializedName("price") var price: String? = null,
        @SerializedName("title") var title: String? = null,
        @SerializedName("runnerInTeam") var runnerInTeam: String? = null,
        @SerializedName("updated_at") var updatedAt: String? = null) : Parcelable {

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
            parcel.readString(),
            parcel.readString())

    fun getTitle(unit: String): String {
        return "$title $distance $unit"
    }

    fun getPriceDisplay(context: Context): String {
        return "${price?.numberDisplayFormat() ?: ""} ${context.getString(R.string.thai_bath)}"
    }

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
        parcel.writeString(runnerInTeam)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ticket> {
        override fun createFromParcel(parcel: Parcel): Ticket {
            return Ticket(parcel)
        }

        override fun newArray(size: Int): Array<Ticket?> {
            return arrayOfNulls(size)
        }
    }
}


