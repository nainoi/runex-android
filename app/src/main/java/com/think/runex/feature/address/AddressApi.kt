package com.think.runex.feature.address

import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.address.data.SubDistrict
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface AddressApi {

    @GET("/api/${ApiConfig.API_VERSION}/tambon/{zipCode}")
    fun getSubDistrictByZipCodeAsync(@Path("zipCode") code: String): Deferred<Result<List<SubDistrict>>>

    @GET("/api/${ApiConfig.API_VERSION}/district/{subDistrict}")
    fun getSubDistrictBySubDistrictAsync(@Path("subDistrict") subDistrict: String): Deferred<Result<List<SubDistrict>>>

    @GET("/api/${ApiConfig.API_VERSION}/amphoe/{district}")
    fun getSubDistrictByDistrictAsync(@Path("district") district: String): Deferred<Result<List<SubDistrict>>>

    @GET("/api/${ApiConfig.API_VERSION}/province/{province}")
    fun getSubDistrictByProvinceAsync(@Path("province") province: String): Deferred<Result<List<SubDistrict>>>
}