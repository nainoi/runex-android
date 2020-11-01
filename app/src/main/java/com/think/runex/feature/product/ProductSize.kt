package com.think.runex.feature.product

import com.google.gson.annotations.SerializedName

data class ProductSize(
        @SerializedName("name") var name: String = "",
        @SerializedName("remark") var remark: String = "")