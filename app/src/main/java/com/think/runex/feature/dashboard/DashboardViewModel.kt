package com.think.runex.feature.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.think.runex.base.BaseViewModel
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.dashboard.data.DashboardInfo
import com.think.runex.feature.event.EventRepository
import com.think.runex.feature.event.data.request.EventDashboardBody
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class DashboardViewModel(private val repo: DashboardRepository) : BaseViewModel() {

    suspend fun getEventDashboard(body: EventDashboardBody): List<DashboardInfo>? = withContext(IO) {
        val result = repo.getEventDashboard(body)
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }

        return@withContext result.data
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DashboardViewModel(DashboardRepository(ApiService().provideService(context, DashboardApi::class.java))) as T
        }
    }
}