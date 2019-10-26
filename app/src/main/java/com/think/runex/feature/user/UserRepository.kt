package com.think.runex.feature.user

import com.think.runex.datasource.Result


class UserRepository(private val remoteDataSource: UserRemoteDataSource) {

    suspend fun getProfile(): Result<Profile> = remoteDataSource.getProfile()
}