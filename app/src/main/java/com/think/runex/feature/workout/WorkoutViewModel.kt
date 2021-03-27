package com.think.runex.feature.workout


import android.graphics.Bitmap
import com.think.runex.base.BaseViewModel
import com.think.runex.common.toByteArray
import com.think.runex.feature.event.data.EventRegisteredForSubmitResult
import com.think.runex.feature.workout.data.WorkoutInfo
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

    suspend fun getWorkoutInfo(workoutId: String): WorkoutInfo? = withContext(IO) {
        val result = repo.getWorkoutInfo(workoutId)
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }
        return@withContext result.data
    }

    suspend fun submitWorkoutToEvents(eventsToSubmit: List<EventRegisteredForSubmitResult>?,
                                      workoutInfo: WorkoutInfo?): Boolean = withContext(IO) {
        var isSuccessAll = true
        eventsToSubmit?.forEach { event ->
            val result = repo.submitWorkoutToEvent(event.eventCode, workoutInfo)
            if (result.isSuccessful().not()) {
                onHandleError(result.statusCode, "Cannot submit workout to event ${event.eventName}", "submit_workout")
                isSuccessAll = false
            }
        }
        return@withContext isSuccessAll
    }
}