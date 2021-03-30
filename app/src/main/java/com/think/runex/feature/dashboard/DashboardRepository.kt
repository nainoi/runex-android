package com.think.runex.feature.dashboard

import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.event.data.request.EventDashboardBody

class DashboardRepository(private val api: DashboardApi) : RemoteDataSource() {

    suspend fun getEventDashboard(body: EventDashboardBody) = call(api.getDashboardAsync(body))
}