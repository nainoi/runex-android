package com.think.runex.feature.auth

import com.think.runex.datasource.Result
import com.think.runex.datasource.remote.ApiUrl
import com.think.runex.datasource.remote.ApiUrl.Companion.KEY_AUTH
import com.think.runex.datasource.remote.ApiUrl.Companion.KEY_VERSION
import com.think.runex.feature.user.Profile
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface AuthApiService {

    @Headers("Accept: application/json")
    @POST("/api/{$KEY_VERSION}/user/ep")
    fun registerAsync(@Body body: RegisterRequest,
                      @Path(KEY_VERSION) version: String = ApiUrl.getApiVersion()): Deferred<AuthResponse>

    @Headers("Accept: application/json")
    @GET("/api/{$KEY_VERSION}/user")
    fun getProfileAsync(@Path(KEY_VERSION) version: String = ApiUrl.getApiVersion(),
                        @Header(KEY_AUTH) token: String = TokenManager.accessToken()): Deferred<Result<Profile>>

    @Headers("Accept: application/json")
    @POST("/api/{$KEY_VERSION}/user/login")
    fun loginAsync(@Body body: LoginRequest,
                   @Path(KEY_VERSION) version: String = ApiUrl.getApiVersion()): Deferred<AuthResponse>
}

