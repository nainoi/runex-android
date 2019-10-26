package com.think.runex.feature.user


import com.think.runex.datasource.Result
import com.think.runex.datasource.remote.ApiUrl
import com.think.runex.feature.auth.TokenManager
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface UserApiService {

    @Headers("Accept: application/json")
    @GET("/api/{${ApiUrl.KEY_VERSION}}/user")
    fun getProfileAsync(@Path(ApiUrl.KEY_VERSION) version: String = ApiUrl.getApiVersion(),
                        @Header(ApiUrl.KEY_AUTH) token: String = TokenManager.accessToken()): Deferred<Result<Profile>>

}