package com.think.runex.feature.workout

import com.google.gson.JsonObject
import com.think.runex.config.AUTHORIZATION
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.data.TokenManager
import com.think.runex.feature.workout.data.WorkoutHistoryMonth
import com.think.runex.feature.workout.data.WorkoutInfo
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface WorkoutApi {

    @POST("/api/${ApiConfig.API_VERSION}/workout")
    fun addWorkoutAsync(
            @Body body: WorkoutInfo,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken()): Deferred<Result<WorkoutInfo>>

    @GET("/api/${ApiConfig.API_VERSION}/workouts/historyAll")
    fun getWorkoutHistoryAsync(
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken()): Deferred<Result<List<WorkoutHistoryMonth>?>>

    @GET("/api/${ApiConfig.API_VERSION}/workoutDetail/{workoutId}")
    fun getWorkoutInfoAsync(
            @Path("workoutId") workoutId: String,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken()): Deferred<Result<WorkoutInfo>>

    @POST("/api/${ApiConfig.API_VERSION}/activity/activitiesWorkout")
    fun submitWorkoutToEventsAsync(
            @Body body: MultipartBody,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken()): Deferred<Result<Any>>

    @Deprecated("Use [submitWorkoutToEventsAsync] instead")
    @POST("/api/${ApiConfig.API_VERSION}/activity/activityWorkout")
    fun submitWorkoutToEventAsync(
            @Body body: JsonObject,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken()): Deferred<Result<Any>>

    @POST("/api/${ApiConfig.API_VERSION}/activity/add")
    fun submitActivityToEventAsync(
            @Body body: MultipartBody,
            @Header(AUTHORIZATION) token: String = TokenManager.accessToken()): Deferred<Result<Any>>
}