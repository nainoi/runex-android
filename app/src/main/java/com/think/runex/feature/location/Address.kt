package com.think.runex.feature.location

import com.google.gson.annotations.SerializedName

data class Address(
        @SerializedName("province") var province: String = "",
        @SerializedName("district") var district: String = "",
        @SerializedName("city") var city: String = "",
        @SerializedName("addr") var addr: String = "",
        @SerializedName("zipcode") var zipCode: String = "")