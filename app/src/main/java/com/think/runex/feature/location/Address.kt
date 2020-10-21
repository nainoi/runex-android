package com.think.runex.feature.location

import com.google.gson.annotations.SerializedName

data class Address(
        @SerializedName("id") var id: String = "",
        @SerializedName("address") var address: String = "",
        @SerializedName("province") var province: String = "",
        @SerializedName("district") var district: String = "",
        @SerializedName("city") var city: String = "",
        @SerializedName("zipcode") var zipCode: String = "",
        @SerializedName("created_at") var createdAt: String = "",
        @SerializedName("updated_at") var updatedAt: String = "")