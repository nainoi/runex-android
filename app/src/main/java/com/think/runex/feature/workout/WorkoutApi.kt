package com.think.runex.feature.workout

import com.think.runex.config.AUTHORIZATION
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.TokenManager
import com.think.runex.feature.workout.model.WorkoutHistoryMonth
import com.think.runex.feature.workout.model.WorkoutInfo
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface WorkoutApi {

    @POST("/api/${ApiConfig.API_VERSION}/workout")
    fun addWorkoutAsync(
            @Body body: WorkoutInfo,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<WorkoutInfo>>

    @GET("/api/${ApiConfig.API_VERSION}/workouts/historyAll")
    fun getWorkoutHistoryAsync(
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<List<WorkoutHistoryMonth>>>
}