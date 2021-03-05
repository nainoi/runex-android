package com.think.runex.feature.workout

import com.think.runex.config.AUTHORIZATION
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.data.TokenManager
import com.think.runex.feature.workout.data.WorkoutHistoryMonth
import com.think.runex.feature.workout.data.WorkoutInfo
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface WorkoutApi {

    @POST("/api/${ApiConfig.API_VERSION}/workout")
    fun addWorkoutAsync(
            @Body body: WorkoutInfo,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<WorkoutInfo>>

    @GET("/api/${ApiConfig.API_VERSION}/workouts/historyAll")
    fun getWorkoutHistoryAsync(
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<List<WorkoutHistoryMonth>>>

    @GET("/api/${ApiConfig.API_VERSION}/workoutDetail/{workoutId}")
    fun getWorkoutInfoAsync(
            @Path("workoutId") workoutId: String,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken): Deferred<Result<WorkoutInfo>>
}