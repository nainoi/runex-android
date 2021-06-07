package com.think.runex.feature.event.team

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.think.runex.base.BaseViewModel
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.event.data.RegisteredData
import com.think.runex.feature.event.data.TicketOptionEventRegistration
import com.think.runex.feature.event.team.data.TeamImage
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

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