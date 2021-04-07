package com.think.runex.feature.workout

import com.google.gson.JsonObject
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.workout.data.WorkoutHistoryMonth
import com.think.runex.feature.workout.data.WorkoutInfo
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.http.*

interface WorkoutApi {

    @POST("/api/${ApiConfig.API_VERSION}/workout")
    fun addWorkoutAsync(@Body body: WorkoutInfo): Deferred<Result<WorkoutInfo>>

    @GET("/api/${ApiConfig.API_VERSION}/workouts/historyAll")
    fun getWorkoutHistoryAsync(): Deferred<Result<List<WorkoutHistoryMonth>?>>

    @GET("/api/${ApiConfig.API_VERSION}/workoutDetail/{workoutId}")
    fun getWorkoutInfoAsync(@Path("workoutId") workoutId: String): Deferred<Result<WorkoutInfo>>

    @POST("/api/${ApiConfig.API_VERSION}/activity/activitiesWorkout")
    fun submitWorkoutToEventsAsync(@Body body: MultipartBody): Deferred<Result<Any>>

    @Deprecated("Use [submitWorkoutToEventsAsync] instead")
    @POST("/api/${ApiConfig.API_VERSION}/activity/activityWorkout")
    fun submitWorkoutToEventAsync(@Body body: JsonObject): Deferred<Result<Any>>

    @POST("/api/${ApiConfig.API_VERSION}/activity/add")
    fun submitActivityToEventAsync(@Body body: MultipartBody): Deferred<Result<Any>>
}