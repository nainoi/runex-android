package com.think.runex.feature.product

import com.google.gson.annotations.SerializedName

data class Product(
        @SerializedName("id") var id: String = "",
        @SerializedName("name") var name: String = "",
        @SerializedName("image") var images: List<ProductImage>? = null,
        @SerializedName("detail") var detail: String = "",
        @SerializedName("status") var status: String = "",
        @SerializedName("reuse") var reuse: Boolean = false,
        @SerializedName("is_show") var isShow: Boolean = false,
        @SerializedName("sizes") var sizes: ProductSize? = null,
        @SerializedName("created_at") var createdAt: String = "",
        @SerializedName("updated_at") var updatedAt: String = "")