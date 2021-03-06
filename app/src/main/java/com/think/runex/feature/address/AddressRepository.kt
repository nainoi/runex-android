package com.think.runex.feature.address

import com.think.runex.datasource.api.RemoteDataSource

class AddressRepository(private val api: AddressApi) : RemoteDataSource() {

    suspend fun getSubDistrictByZipCode(zipCode: String) = call(api.getSubDistrictByZipCodeAsync(zipCode))

    suspend fun getSubDistrictBySubDistrict(subDistrict: String) = call(api.getSubDistrictBySubDistrictAsync(subDistrict))

    suspend fun getSubDistrictByDistrict(district: String) = call(api.getSubDistrictByDistrictAsync(district))

    suspend fun getSubDistrictByProvince(province: String) = call(api.getSubDistrictByProvinceAsync(province))
}