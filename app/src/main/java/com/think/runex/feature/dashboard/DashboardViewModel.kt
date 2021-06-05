package com.think.runex.feature.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jozzee.android.core.datetime.toTimeMillis
import com.think.runex.base.BaseViewModel
import com.think.runex.config.SERVER_DATE_TIME_FORMAT
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.dashboard.data.DashboardInfo
import com.think.runex.feature.dashboard.data.DashboardInfoRequestBody
import com.think.runex.feature.dashboard.data.DeleteActivityBody
import com.think.runex.feature.dashboard.data.UserActivityDashboard
import com.think.runex.feature.event.data.Ticket
import com.think.runex.feature.workout.data.WorkoutHistoryMonth
import com.think.runex.feature.workout.data.WorkoutInfo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class DashboardViewModel(private val repo: DashboardRepository) : BaseViewModel() {

    var dashboardInfo: DashboardInfo? = null
        private set

    var myUserId: String = ""
        private set

    var isTeamLeader: Boolean = false
        private set

    suspend fun getDashboardInfo(
        eventCode: String,
        orderId: String,
        registerId: String,
        parentRegisterId: String
    ): DashboardInfo? = withContext(IO) {

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

        if (result.isSuccessful()) {

            //Sort data before completed
            result.data?.activityList?.forEach { userDashboard ->
                userDashboard.activityInfoList = userDashboard.activityInfoList?.sortedByDescending {
                    it.getActivityDateTimeMillis()
                }?.toMutableList()
            }

            //Update dashboard data
            dashboardInfo = result.data
            isTeamLeader = dashboardInfo?.isTeamLeader(myUserId) ?: false

        } else {
            onHandleError(result.code, result.message, "dashboard")
        }

        return@withContext dashboardInfo
    }

    suspend fun deleteActivity(activityToDelete: DeleteActivityBody): DashboardInfo? = withContext(IO) {

        val result = repo.deleteActivity(activityToDelete)

        if (result.isSuccessful()) {

            val dashboardResult = repo.getDashboardInfo(
                DashboardInfoRequestBody(
                    activityToDelete.eventCode,
                    activityToDelete.orderId,
                    activityToDelete.registerId,
                    activityToDelete.parentRegisterId
                )
            )

            if (dashboardResult.isSuccessful()) {
                dashboardInfo = dashboardResult.data
                return@withContext dashboardInfo
            } else {
                onHandleError(result.code, result.message)
            }

        } else {
            onHandleError(result.code, result.message)
        }

        return@withContext null
    }

    fun getTicketAtRegister(): Ticket? = dashboardInfo?.registered?.getTicketAtRegister()

    fun getEventId() = dashboardInfo?.registered?.eventDetail?.id ?: 0

    fun getRegistrationDataForEdit() = dashboardInfo?.getRegistrationDataForEdit(myUserId)

    fun getRegistrationName(userId: String?): String {
        return dashboardInfo?.registered?.registeredDataList?.find {
            it.userId == userId
        }?.ticketOptions?.firstOrNull()?.userOption?.getFullName() ?: ""
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DashboardViewModel(
                DashboardRepository(
                    ApiService().provideService(
                        context,
                        DashboardApi::class.java
                    )
                )
            ) as T
        }
    }
}