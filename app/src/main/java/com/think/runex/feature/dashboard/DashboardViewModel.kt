package com.think.runex.feature.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.think.runex.base.BaseViewModel
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.dashboard.data.DashboardInfo
import com.think.runex.feature.dashboard.data.DashboardInfoRequestBody
import com.think.runex.feature.event.data.Ticket
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class DashboardViewModel(private val repo: DashboardRepository) : BaseViewModel() {

    private var dashboardInfo: DashboardInfo? = null

    var myUserId: String = ""
        private set

    var isTeamLeader: Boolean = false
        private set

    suspend fun getDashboardInfo(eventCode: String,
                                 orderId: String,
                                 registerId: String,
                                 parentRegisterId: String,
                                 forceRefresh: Boolean = false): DashboardInfo? = withContext(IO) {

        if (dashboardInfo != null && forceRefresh.not()) {
            return@withContext dashboardInfo
        }

        //Get my user info for check is team leader.
        if (myUserId.isBlank()) {
            val myUserInfoResult = repo.getMyUserInfo()
            if (myUserInfoResult.isSuccessful().not()) {
                onHandleError(myUserInfoResult.code, myUserInfoResult.message, "dashboard")
                return@withContext null
            }
            myUserId = myUserInfoResult.data?.id ?: ""
        }

        //Get dashboard info.
        val result = repo.getDashboardInfo(DashboardInfoRequestBody(eventCode, orderId, registerId, parentRegisterId))

        when (result.isSuccessful()) {
            true -> {
                //Update dashboard data
                dashboardInfo = result.data
                isTeamLeader = dashboardInfo?.isTeamLeader(myUserId) ?: false
            }
            false -> onHandleError(result.code, result.message, "dashboard")
        }

        return@withContext dashboardInfo
    }

    fun getTicketAtRegister(): Ticket? = dashboardInfo?.registered?.getTicketAtRegister()

    fun getEventId() = dashboardInfo?.registered?.eventDetail?.id ?: 0

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DashboardViewModel(DashboardRepository(ApiService().provideService(context, DashboardApi::class.java))) as T
        }
    }
}