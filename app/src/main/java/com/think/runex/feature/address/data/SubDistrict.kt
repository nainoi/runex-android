package com.think.runex.feature.address.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class SubDistrict(
        @SerializedName("id") var id: String? = "",
        @SerializedName("district") var subDistrict: String? = "",
        @SerializedName("amphoe") var district: String? = "",
        @SerializedName("province") var province: String? = "",
        @SerializedName("zipcode") var zipCode: Int? = 0,
        @SerializedName("district_code") var subDistrictCode: Int? = 0,
        @SerializedName("amphoe_code") var districtCode: Int? = 0,
        @SerializedName("province_code") var provinceCode: Int? = 0) : Parcelable {

    companion object CREATOR : Parcelable.Creator<SubDistrict> {
        override fun createFromParcel(parcel: Parcel): SubDistrict {
            return SubDistrict(parcel)
        }

        override fun newArray(size: Int): Array<SubDistrict?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(subDistrict)
        parcel.writeString(district)
        parcel.writeString(province)
        parcel.writeValue(zipCode)
        parcel.writeValue(subDistrictCode)
        parcel.writeValue(districtCode)
        parcel.writeValue(provinceCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun getFullAddress(): String {
        return "$subDistrict, $district, $province, $zipCode"
    }
}