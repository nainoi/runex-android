package com.think.runex.feature.auth

import com.think.runex.datasource.Result
import com.think.runex.feature.user.Profile


class AuthRepository(private val remoteDataSource: AuthRemoteDataSource,
                     private val localDataSource: AuthLocalDataSource) {

    fun getAccessToken() = localDataSource.getAccessToken()

    fun setAccessToken(accessToken: AccessToken) = localDataSource.setAccessToken(accessToken)

    suspend fun register(body: RegisterRequest): Result<AccessToken> = remoteDataSource.register(body)

    suspend fun login(body: LoginRequest): Result<AccessToken> = remoteDataSource.login(body)

    suspend fun getProfile(): Result<Profile> = remoteDataSource.getProfile()

}