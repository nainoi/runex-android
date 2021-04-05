package com.think.runex.feature.workout

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.think.runex.util.extension.toJson
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.event.data.EventForSubmitResult
import com.think.runex.feature.workout.data.WorkoutInfo
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class WorkoutRepository(private val api: WorkoutApi) : RemoteDataSource() {

    suspend fun addWorkout(body: WorkoutInfo) = call(api.addWorkoutAsync(body))

    suspend fun getWorkoutHistory() = call(api.getWorkoutHistoryAsync())

    suspend fun getWorkoutInfo(workoutId: String) = call(api.getWorkoutInfoAsync(workoutId))

    suspend fun submitWorkoutToEvent(eventCode: String, workoutInfo: WorkoutInfo?): Result<Any> {

        val jsonObject = JsonObject().apply {
            addProperty("event_code", eventCode)
            add("workout_info", Gson().toJsonTree(workoutInfo))
        }

        val body = jsonObject.toJson().toRequestBody("application/json; charset=utf-8".toMediaType())

        return call(api.submitWorkoutToEventAsync(body))
    }

    suspend fun submitWorkoutToEvents(eventsToSubmit: List<EventForSubmitResult>?,
                                      workoutInfo: WorkoutInfo?,
                                      workoutImage: ByteArray?): Result<Any> {

        val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("event_activity", eventsToSubmit?.toJson() ?: "")
                .addFormDataPart("workout_info", workoutInfo?.toJson() ?: "")
                .addFormDataPart(
                        name = "image",
                        filename = "workout-image",
                        body = (workoutImage ?: byteArrayOf()).toRequestBody())
                .build()


        return call(api.submitWorkoutToEventsAsync(multipartBody))
    }
}