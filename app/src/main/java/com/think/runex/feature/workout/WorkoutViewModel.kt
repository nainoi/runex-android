package com.think.runex.feature.workout


import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.think.runex.base.BaseViewModel
import com.think.runex.util.extension.toByteArray
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.event.data.EventForSubmitResult
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

    suspend fun submitWorkoutToEventsManual(eventsToSubmit: List<EventForSubmitResult>?,
                                            workoutInfo: WorkoutInfo?): Boolean = withContext(IO) {
        var isSuccessAll = true
        eventsToSubmit?.forEach { event ->
            val result = repo.submitWorkoutToEvent(event.eventCode, workoutInfo)
            if (result.isSuccessful().not()) {
                onHandleError(result.statusCode, "Cannot submit workout to event", "submit_workout")
                isSuccessAll = false
            }
        }
        return@withContext isSuccessAll
    }

    suspend fun submitWorkoutToEvents(eventsToSubmit: List<EventForSubmitResult>?,
                                      workoutInfo: WorkoutInfo?,
                                      workoutImage: Bitmap?): Boolean = withContext(IO) {

        val result = repo.submitWorkoutToEvents(eventsToSubmit, workoutInfo, workoutImage?.toByteArray())
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message, "submit_workout")
        }

        return@withContext result.isSuccessful()
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WorkoutViewModel(WorkoutRepository(ApiService().provideService(context, WorkoutApi::class.java))) as T
        }
    }
}