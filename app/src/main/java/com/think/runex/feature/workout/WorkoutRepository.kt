package com.think.runex.feature.workout

import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.workout.data.WorkoutInfo

class WorkoutRepository(private val api: WorkoutApi) : RemoteDataSource() {

    suspend fun addWorkout(body: WorkoutInfo) = call(api.addWorkoutAsync(body))

    suspend fun getWorkoutHistory() = call(api.getWorkoutHistoryAsync())

    suspend fun getWorkoutInfo(workoutId: String) = call(api.getWorkoutInfoAsync(workoutId))
}