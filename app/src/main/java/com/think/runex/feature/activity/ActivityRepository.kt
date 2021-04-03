package com.think.runex.feature.activity

import com.think.runex.datasource.api.RemoteDataSource

class ActivityRepository(private val api: ActivityApi) : RemoteDataSource() {
}