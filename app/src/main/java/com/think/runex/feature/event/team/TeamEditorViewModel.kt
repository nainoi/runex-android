package com.think.runex.feature.event.team

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.think.runex.base.BaseViewModel
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.event.data.RegisteredData
import com.think.runex.feature.event.data.TicketOptionEventRegistration
import com.think.runex.feature.event.team.data.TeamImage
import com.think.runex.util.UploadImageUtil
import com.think.runex.util.extension.getDisplayName
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class TeamEditorViewModel(private val repo: TeamRepository) : BaseViewModel() {

    var registerData: RegisteredData? = null

    suspend fun updateTeamInfo(
        team: String?,
        color: String?,
        zone: String?
    ): Boolean = withContext(IO) {

        val ticketOption = registerData?.ticketOptions?.get(0) ?: TicketOptionEventRegistration()
        ticketOption.userOption?.apply {
            this.team = team ?: ""
            this.color = color ?: ""
            this.zone = zone ?: ""
        }

        val body = JsonObject().apply {
            addProperty("event_code", registerData?.eventCode ?: "")
            addProperty("reg_id", registerData?.id ?: "")
            add("ticket_options", Gson().toJsonTree(ticketOption))
        }

        val result = repo.updateTeamInfo(body)

        if (result.isSuccessful()) {
            registerData?.ticketOptions?.get(0)?.userOption?.apply {
                this.team = team ?: ""
                this.color = color ?: ""
                this.zone = zone ?: ""
            }
        } else {
            onHandleError(result.code, result.message)
        }

        return@withContext result.isSuccessful()
    }

    suspend fun getTeamImageUrl(): String? = withContext(IO) {

        val result = repo.getTeamImage(TeamImage(registerData?.id))

        if (result.isSuccessful().not()) {
            onHandleError(result.code, result.message)
        }

        return@withContext result.data?.iconUrl
    }

    suspend fun updateTeamImage(context: Context, imageUri: Uri): String? = withContext(IO) {

        //Upload new profile image to server.
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("reg_id", registerData?.id ?: "")
            .addFormDataPart(
                name = "image",
                filename = imageUri.getDisplayName(context) ?: "team-image",
                body = UploadImageUtil().reduceImageSize(context, imageUri).toRequestBody()
            )
            .build()

        val result = repo.uploadTeamImage(body)

        if (result.isSuccessful().not()) {
            onHandleError(result.code, result.message)
        }

        return@withContext result.data?.iconUrl
    }

    override fun onCleared() {
        super.onCleared()
        registerData = null
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TeamEditorViewModel(TeamRepository(ApiService().provideService(context, TeamApi::class.java))) as T
        }
    }

}