package com.think.runex.feature.workout

import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.workout.model.WorkoutInfo

class WorkoutRepository(private val api: WorkoutApi) : RemoteDataSource() {

    suspend fun addWorkout(body: WorkoutInfo) = call(api.addWorkoutAsync(body))
}