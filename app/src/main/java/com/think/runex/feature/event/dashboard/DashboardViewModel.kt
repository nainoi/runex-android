package com.think.runex.feature.event.dashboard

import com.think.runex.base.BaseViewModel
import com.think.runex.feature.event.EventRepository
import com.think.runex.feature.event.data.EventDashboard
import com.think.runex.feature.event.data.request.EventDashboardBody
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class DashboardViewModel(private val repo: EventRepository) : BaseViewModel() {

    suspend fun getEventDashboard(body: EventDashboardBody): EventDashboard? = withContext(IO) {
        val result = repo.getEventDashboard(body)
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }
        if (result.data?.isNotEmpty() == true) {
            return@withContext result.data?.get(0)
        }
        return@withContext null
    }
}