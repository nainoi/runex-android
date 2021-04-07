package com.think.runex.feature.activity

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.think.runex.base.BaseViewModel
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.activity.data.ActivityForSubmitEvent
import com.think.runex.feature.workout.WorkoutApi
import com.think.runex.feature.workout.data.WorkoutType
import com.think.runex.util.UploadImageUtil
import com.think.runex.util.extension.getDisplayName
import com.think.runex.util.extension.getExtensionFromMimeType
import com.think.runex.util.extension.toJson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.HttpURLConnection
import javax.net.ssl.HttpsURLConnection

class ActivityViewModel(private val repo: ActivityRepository) : BaseViewModel() {

    suspend fun submitActivityToEvent(context: Context,
                                      imageUri: Uri?,
                                      distance: Double,
                                      activityDate: String,
                                      caption: String,
                                      activityForSubmit: ActivityForSubmitEvent?): Boolean = withContext(IO) {

        val activityInfoObject = JsonObject().apply {
            addProperty("activity_type", WorkoutType.RUNNING)
            addProperty("caption", caption)
            addProperty("distance", distance)
            addProperty("activity_date", activityDate)
        }

        val body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("activity_info", activityInfoObject.toJson())
                .addFormDataPart("event_code", activityForSubmit?.eventCode ?: "")
                .addFormDataPart("order_id", activityForSubmit?.orderId ?: "")
                .addFormDataPart("reg_id", activityForSubmit?.registerId ?: "")
                .addFormDataPart("parent_reg_id", activityForSubmit?.parentRegisterId ?: "")
                .addFormDataPart("ticket", activityForSubmit?.ticket?.toJson() ?: "")
                .addFormDataPart("user_id", activityForSubmit?.userId ?: "")

        if (imageUri != null) {
            body.addFormDataPart(name = "image",
                    filename = "activity-image",
                    body = UploadImageUtil().reduceImageSize(context, imageUri).toRequestBody())
        }

        val result = repo.submitActivityToEvent(body.build())

        if (result.isSuccessful().not()) {
            onHandleError(result.code, result.message)
        }

        return@withContext result.isSuccessful()
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ActivityViewModel(ActivityRepository(ApiService().provideService(context, WorkoutApi::class.java))) as T
        }
    }
}