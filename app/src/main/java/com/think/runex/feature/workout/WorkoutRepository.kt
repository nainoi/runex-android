package com.think.runex.feature.workout

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.think.runex.common.getDisplayName
import com.think.runex.common.toJson
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.event.data.EventRegisteredForSubmitResult
import com.think.runex.feature.workout.data.WorkoutInfo
import com.think.runex.util.UploadImageUtil
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class WorkoutRepository(private val api: WorkoutApi) : RemoteDataSource() {

    suspend fun addWorkout(body: WorkoutInfo) = call(api.addWorkoutAsync(body))

    suspend fun getWorkoutHistory() = call(api.getWorkoutHistoryAsync())

    suspend fun getWorkoutInfo(workoutId: String) = call(api.getWorkoutInfoAsync(workoutId))

//    suspend fun submitWorkoutToEvents(workoutInfo: WorkoutInfo?,
//                                      eventsToSubmit: List<EventRegisteredForSubmitResult>?): Result<Any> {
//
//        val jsonObject = JsonObject().apply {
//            add("event_activity", Gson().toJsonTree(eventsToSubmit))
//            add("workout_info", Gson().toJsonTree(workoutInfo))
//        }
//        val multipartBody = MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                //.addFormDataPart("event_activity", eventsToSubmit?.toJson() ?: "")
//                //.addFormDataPart("workout_info", workoutInfo?.toJson() ?: "")
//                .addFormDataPart("data", jsonObject.toJson())
//                .build()
//
//        return call(api.submitWorkoutToEventsAsync(multipartBody))
//    }

    suspend fun submitWorkoutToEvent(eventCode: String, workoutInfo: WorkoutInfo?): Result<Any> {

        val jsonObject = JsonObject().apply {
            addProperty("event_code", eventCode)
            add("workout_info", Gson().toJsonTree(workoutInfo))
        }

        val body = jsonObject.toJson().toRequestBody("application/json; charset=utf-8".toMediaType())

        return call(api.submitWorkoutToEventAsync(body))
    }
}