package com.think.runex.feature.event.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.think.runex.feature.product.Product

data class Ticket(
        @SerializedName("category") var category: String? = null,
        @SerializedName("created_at") var createdAt: String? = null,
        @SerializedName("currency") var currency: String? = null,
        @SerializedName("description") var description: String? = null,
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
        @SerializedName("products") var products: Product? = null,
        @SerializedName("quantity") var quantity: Int? = 0,
        @SerializedName("team") var team: Int? = 0,
        @SerializedName("ticket_type") var ticketType: String? = null,
        @SerializedName("title") var title: String? = null,
        @SerializedName("updated_at") var updatedAt: String? = null) : Parcelable {

    companion object CREATOR : Parcelable.Creator<Ticket> {
        override fun createFromParcel(parcel: Parcel): Ticket {
            return Ticket(parcel)
        }

        override fun newArray(size: Int): Array<Ticket?> {
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
            parcel.readString(),
            parcel.readString(),
            null,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Product::class.java.classLoader),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(category)
        parcel.writeString(createdAt)
        parcel.writeString(currency)
        parcel.writeString(description)
        parcel.writeString(detail)
        parcel.writeString(distance)
        parcel.writeString(eventId)
        parcel.writeString(id)
        parcel.writeString(limit)
        parcel.writeString(photoMap)
        parcel.writeString(photoMedal)
        parcel.writeString(photoShirt)
        parcel.writeString(price)
        parcel.writeParcelable(products, flags)
        parcel.writeValue(quantity)
        parcel.writeValue(team)
        parcel.writeString(ticketType)
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


