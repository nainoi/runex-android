package com.think.runex.feature.event.dashboard

import com.think.runex.base.BaseViewModel
import com.think.runex.feature.event.EventRepository
import com.think.runex.feature.event.data.DashboardInfo
import com.think.runex.feature.event.data.request.EventDashboardBody
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class DashboardViewModel(private val repo: EventRepository) : BaseViewModel() {

    suspend fun getEventDashboard(body: EventDashboardBody): List<DashboardInfo>? = withContext(IO) {
        val result = repo.getEventDashboard(body)
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }

        return@withContext result.data
    }
}