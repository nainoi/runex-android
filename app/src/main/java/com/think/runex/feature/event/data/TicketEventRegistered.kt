package com.think.runex.feature.event.data

import com.google.gson.annotations.SerializedName
import com.think.runex.feature.product.Product

data class TicketEventRegistered(
        @SerializedName("created_at") var createdAt: String? = null,
        @SerializedName("currency") var currency: String? = null,
        @SerializedName("description") var description: String? = null,
        @SerializedName("distance") var distance: Float? = null,
        @SerializedName("id") var id: String? = null,
        @SerializedName("price") var price: Float? = null,
        @SerializedName("products") var products: Product? = null,
        @SerializedName("quantity") var quantity: Int? = 0,
        @SerializedName("team") var team: Int? = 0,
        @SerializedName("ticket_type") var ticketType: String? = null,
        @SerializedName("title") var title: String? = null,
        @SerializedName("updated_at") var updatedAt: String? = null)