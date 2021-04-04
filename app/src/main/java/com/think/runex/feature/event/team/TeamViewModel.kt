package com.think.runex.feature.event.team

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.think.runex.base.BaseViewModel
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.event.EventApi
import com.think.runex.feature.event.data.EventRegistered
import com.think.runex.feature.user.data.UserInfo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class TeamViewModel(private val repo: TeamRepository) : BaseViewModel() {

    suspend fun getEventRegisteredInfo(eventCode: String, registerId: String, parentRegisterId: String): EventRegistered? = withContext(IO) {

        val body = JsonObject().apply {
            addProperty("event_code", eventCode)
            addProperty("reg_id", registerId)
            addProperty("parent_reg_id", parentRegisterId)
        }

        val result = repo.getEventRegisteredInfo(body)
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }

        return@withContext result.data
    }

    suspend fun getUserInfoById(userId: String): UserInfo? = withContext(IO) {
        val result = repo.getUserInfoById(userId)
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }
        return@withContext result.data
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TeamViewModel(TeamRepository(ApiService().provideService(context, EventApi::class.java))) as T
        }
    }
}