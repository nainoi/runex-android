package com.think.runex.feature.ticket

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
        @SerializedName("updated_at") var updatedAt: String = "")