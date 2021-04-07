package com.think.runex.feature.workout


import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.think.runex.base.BaseViewModel
import com.think.runex.util.extension.toByteArray
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.event.data.EventForSubmitResult
import com.think.runex.feature.workout.data.WorkoutInfo
import com.think.runex.util.extension.toJson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class WorkoutViewModel(private val repo: WorkoutRepository) : BaseViewModel() {

    suspend fun addWorkout(workout: WorkoutInfo): WorkoutInfo? = withContext(IO) {

        val result = repo.addWorkout(workout)

        if (result.isSuccessful().not()) {
            onHandleError(result.code, result.message)

        }
        return@withContext result.data
    }

    suspend fun getWorkoutInfo(workoutId: String): WorkoutInfo? = withContext(IO) {

        val result = repo.getWorkoutInfo(workoutId)

        if (result.isSuccessful().not()) {
            onHandleError(result.code, result.message)
        }

        return@withContext result.data
    }

    /** @deprecated
     *  Submit workout each a event by for loop
     */
    @Deprecated("Use [submitWorkoutToEvents] instead")
    suspend fun submitWorkoutToEventsManual(eventsToSubmit: List<EventForSubmitResult>?,
                                            workoutInfo: WorkoutInfo?): Boolean = withContext(IO) {
        var isSuccessAll = true

        eventsToSubmit?.forEach { event ->

            val body = JsonObject().apply {
                addProperty("event_code", event.eventCode)
                add("workout_info", Gson().toJsonTree(workoutInfo))
            }

            val result = repo.submitWorkoutToEvent(body)

            if (result.isSuccessful().not()) {
                onHandleError(result.code, "Cannot submit workout to event", "submit_workout")
                isSuccessAll = false
            }
        }
        return@withContext isSuccessAll
    }

    suspend fun submitWorkoutToEvents(eventsToSubmit: List<EventForSubmitResult>?,
                                      workoutInfo: WorkoutInfo?,
                                      workoutImage: Bitmap?): Boolean = withContext(IO) {

        val body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("event_activity", eventsToSubmit?.toJson() ?: "")
                .addFormDataPart("workout_info", workoutInfo?.toJson() ?: "")
                .addFormDataPart(
                        name = "image",
                        filename = "workout-image",
                        body = (workoutImage?.toByteArray() ?: byteArrayOf()).toRequestBody())
                .build()


        val result = repo.submitWorkoutToEvents(body)

        if (result.isSuccessful().not()) {
            onHandleError(result.code, result.message, "submit_workout")
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