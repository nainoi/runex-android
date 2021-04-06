package com.think.runex.feature.activity

import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.workout.WorkoutApi
import okhttp3.MultipartBody

class ActivityRepository(private val api: WorkoutApi) : RemoteDataSource() {

    suspend fun submitActivityToEvent(body: MultipartBody): Result<Any> = call(api.submitActivityToEventAsync(body))

}