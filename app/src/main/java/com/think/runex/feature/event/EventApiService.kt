package com.think.runex.feature.event

import com.think.runex.datasource.Result
import com.think.runex.datasource.remote.ApiUrl
import com.think.runex.feature.auth.TokenManager
import com.think.runex.feature.product.Product
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import java.util.ArrayList

interface EventApiService {

    @Headers("Accept: application/json")
    @GET("/api/{${ApiUrl.KEY_VERSION}}/event/all")
    fun getEventListAsync(@Path(ApiUrl.KEY_VERSION) version: String = ApiUrl.getApiVersion()): Deferred<Result<ArrayList<Event>>>

    @Headers("Accept: application/json")
    @GET("/api/{${ApiUrl.KEY_VERSION}}/event/findByStatus/{status}")
    fun getEventListByStatusAsync(
            @Path("status") eventId: String,
            @Path(ApiUrl.KEY_VERSION) version: String = ApiUrl.getApiVersion(),
            @Header(ApiUrl.KEY_AUTH) token: String = TokenManager.accessToken()): Deferred<Result<ArrayList<Event>>>

    @Headers("Accept: application/json")
    @GET("/api/{${ApiUrl.KEY_VERSION}}/event/eventInfo/{eventId}")
    fun getEventInfoAsync(
            @Path("eventId") eventId: String,
            @Path(ApiUrl.KEY_VERSION) version: String = ApiUrl.getApiVersion(),
            @Header(ApiUrl.KEY_AUTH) token: String = TokenManager.accessToken()): Deferred<Result<EventInfo>>

    @Headers("Accept: application/json")
    @GET("/api/{${ApiUrl.KEY_VERSION}}/event/getProduct/{eventId}")
    fun getProductListInEventAsync(
            @Path("eventId") eventId: String,
            @Path(ApiUrl.KEY_VERSION) version: String = ApiUrl.getApiVersion(),
            @Header(ApiUrl.KEY_AUTH) token: String = TokenManager.accessToken()): Deferred<Result<ArrayList<Product>>>
}