package com.think.runex.feature.user

import com.think.runex.datasource.Result
import com.think.runex.datasource.remote.RemoteDataSource

class UserRemoteDataSource(private val service: UserApiService) : RemoteDataSource() {

    suspend fun getProfile(): Result<Profile> = request(service.getProfileAsync())
}