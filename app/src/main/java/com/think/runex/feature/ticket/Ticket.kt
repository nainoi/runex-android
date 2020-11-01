package com.think.runex.feature.ticket

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.think.runex.feature.product.Product

data class Ticket(
        @SerializedName("id") var id: String? = "",
        @SerializedName("title") var title: String? = "",
        @SerializedName("price") var price: Double? = 0.0,
        @SerializedName("description") var description: String? = "",
        @SerializedName("currency") var currency: String? = "",
        @SerializedName("ticket_type") var ticketType: String? = "",
        @SerializedName("team") var team: Int? = 0,
        @SerializedName("quantity") var quantity: Int? = 0,
        @SerializedName("distance") var distance: Float? = 0f,
        @SerializedName("product") var products: List<Product>? = null,
        @SerializedName("created_at") var createdAt: String = "",
        @SerializedName("updated_at") var updatedAt: String = "") : Parcelable {

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
            parcel.readValue(Double::class.java.classLoader) as? Double,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Float::class.java.classLoader) as? Float,
            parcel.createTypedArrayList(Product),
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeValue(price)
        parcel.writeString(description)
        parcel.writeString(currency)
        parcel.writeString(ticketType)
        parcel.writeValue(team)
        parcel.writeValue(quantity)
        parcel.writeValue(distance)
        parcel.writeTypedList(products)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }
}