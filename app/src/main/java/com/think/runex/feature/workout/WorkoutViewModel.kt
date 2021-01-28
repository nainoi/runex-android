package com.think.runex.feature.workout

import com.think.runex.datasource.BaseViewModel
import com.think.runex.feature.workout.model.WorkoutInfo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext


class WorkoutViewModel(private val repo: WorkoutRepository) : BaseViewModel() {

    suspend fun addWorkout(workout: WorkoutInfo): WorkoutInfo? = withContext(IO) {
        val result = repo.addWorkout(workout)
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }
        return@withContext result.data
    }
}