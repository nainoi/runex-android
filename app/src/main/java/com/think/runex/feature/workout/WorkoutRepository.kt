package com.think.runex.feature.workout

import com.google.gson.JsonObject
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.workout.data.WorkoutInfo
import okhttp3.MultipartBody

class WorkoutRepository(private val api: WorkoutApi) : RemoteDataSource() {

    suspend fun addWorkout(body: WorkoutInfo) = call(api.addWorkoutAsync(body))

    suspend fun getWorkoutHistory() = call(api.getWorkoutHistoryAsync())

    suspend fun getWorkoutInfo(workoutId: String) = call(api.getWorkoutInfoAsync(workoutId))

    @Deprecated("Use [submitWorkoutToEvents] instead")
    suspend fun submitWorkoutToEvent(body: JsonObject): Result<Any> = call(api.submitWorkoutToEventAsync(body))

    suspend fun submitWorkoutToEvents(body: MultipartBody): Result<Any> = call(api.submitWorkoutToEventsAsync(body))
}