package com.think.runex.feature.user

import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource


class UserRepository(private val api: UserApi) : RemoteDataSource() {

    suspend fun userInfo(): Result<UserInfo> = call(api.getUserInfoAsync())
}