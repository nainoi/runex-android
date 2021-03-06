package com.think.runex.feature.address

import com.google.gson.annotations.SerializedName

data class SubDistrict(
        @SerializedName("id") var id: String? = null,
        @SerializedName("district") var subDistrict: String? = null,
        @SerializedName("amphoe") var district: String? = null,
        @SerializedName("province") var province: String? = null,
        @SerializedName("zipcode") var zipCode: Int? = 0,
        @SerializedName("district_code") var subDistrictCode: Int? = 0,
        @SerializedName("amphoe_code") var districtCode: Int? = 0,
        @SerializedName("province_code") var provinceCode: Int? = 0)