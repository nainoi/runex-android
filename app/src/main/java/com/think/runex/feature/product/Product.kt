package com.think.runex.feature.product

import com.google.gson.annotations.SerializedName

data class Product(
        @SerializedName("name") var name: String = "",
        @SerializedName("image") var images: List<ProductImage>? = null,
        @SerializedName("detail") var detail: String = "",
        @SerializedName("type") var types: List<ProductType>? = null,
        @SerializedName("unit") var unit: Int = 0,
        @SerializedName("status") var status: String = "",
        @SerializedName("created_at") var createdAt: String = "",
        @SerializedName("updated_at") var updatedAt: String = "") : ProductId()