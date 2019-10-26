package com.think.runex.feature.activity

import com.think.runex.datasource.remote.ApiUrl
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ActivityApiService {


    @Headers("Accept: application/json")
    @POST("/api/{${ApiUrl.KEY_VERSION}}/activity/add")
    fun addActivityAsync(
            @Body body: AddActivityRequest,
            @Path(ApiUrl.KEY_VERSION) version: String = ApiUrl.getApiVersion())
}