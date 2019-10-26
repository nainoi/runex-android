package com.think.runex.feature.ticket

import com.google.gson.annotations.SerializedName
import com.think.runex.feature.product.ProductId

data class Ticket(
        @SerializedName("id") var id: String = "",
        @SerializedName("product") var products: List<ProductId>? = null,
        @SerializedName("price") var price: Float = 0f,
        @SerializedName("created_at") var createdAt: String = "",
        @SerializedName("updated_at") var updatedAt: String = "")