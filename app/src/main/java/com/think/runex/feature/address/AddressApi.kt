package com.think.runex.feature.address

import com.think.runex.config.AUTHORIZATION
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.address.data.SubDistrict
import com.think.runex.feature.auth.data.TokenManager
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AddressApi {

    @GET("/api/${ApiConfig.API_VERSION}/tambon/{zipCode}")
    fun getSubDistrictByZipCodeAsync(
            @Path("zipCode") code: String,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<List<SubDistrict>>>


    @GET("/api/${ApiConfig.API_VERSION}/district/{subDistrict}")
    fun getSubDistrictBySubDistrictAsync(
            @Path("subDistrict") subDistrict: String,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<List<SubDistrict>>>

    @GET("/api/${ApiConfig.API_VERSION}/amphoe/{district}")
    fun getSubDistrictByDistrictAsync(
            @Path("district") district: String,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<List<SubDistrict>>>

    @GET("/api/${ApiConfig.API_VERSION}/province/{province}")
    fun getSubDistrictByProvinceAsync(
            @Path("province") province: String,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<List<SubDistrict>>>
}