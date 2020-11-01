package com.think.runex.feature.user

import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.TokenManager
import com.think.runex.util.KEY_AUTH
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Header

interface UserApi {

    @GET("/api/${ApiConfig.API_VERSION}/user")
    fun getUserInfoAsync(@Header(KEY_AUTH) token: String = TokenManager.accessToken): Deferred<Result<UserInfo>>
}