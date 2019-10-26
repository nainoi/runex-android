package com.think.runex.feature.product

import com.google.gson.annotations.SerializedName

data class ProductType(
        @SerializedName("name") var name: String = "",
        @SerializedName("remark") var remark: String = "",
        @SerializedName("price") var price: Float = 0f)