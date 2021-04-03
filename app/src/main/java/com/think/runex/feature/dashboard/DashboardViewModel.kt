package com.think.runex.feature.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.think.runex.base.BaseViewModel
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.activity.data.AddActivityBody
import com.think.runex.feature.dashboard.data.DashboardInfo
import com.think.runex.feature.event.EventRepository
import com.think.runex.feature.event.data.request.EventDashboardBody
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class DashboardViewModel(private val repo: DashboardRepository) : BaseViewModel() {

    private var dashboardInfo: DashboardInfo? = null

    suspend fun getEventDashboard(body: EventDashboardBody): DashboardInfo? = withContext(IO) {

        if (dashboardInfo != null) {
            return@withContext dashboardInfo
        }

        val result = repo.getEventDashboard(body)
        when (result.isSuccessful()) {
            true -> dashboardInfo = result.data
            false -> onHandleError(result.statusCode, result.message, "dashboard")
        }

        return@withContext dashboardInfo
    }

    fun getUserFullNameFromActivityPosition(position: Int): String {
        if (position < dashboardInfo?.eventRegistered?.eventRegisteredList?.size ?: 0 &&
                dashboardInfo?.eventRegistered?.eventRegisteredList?.get(position)?.ticketOptions?.isNotEmpty() == true) {
            return dashboardInfo?.eventRegistered?.eventRegisteredList?.get(position)?.ticketOptions?.get(0)?.userOption?.let { userOption ->
                if (userOption.firstName.isNotBlank() && userOption.lastName.isNotBlank()) {
                    "${userOption.firstName} ${userOption.lastName}"
                } else {
                    userOption.fullName
                }
            } ?: ""
        }
        return ""
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DashboardViewModel(DashboardRepository(ApiService().provideService(context, DashboardApi::class.java))) as T
        }
    }
}