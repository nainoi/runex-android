package com.think.runex.feature.event.team

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.think.runex.base.BaseViewModel
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.event.EventApi
import com.think.runex.feature.event.data.EventDetail
import com.think.runex.feature.event.data.Registered
import com.think.runex.feature.event.data.RegisterData
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.util.extension.launch
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class TeamViewModel(private val repo: TeamRepository) : BaseViewModel() {

    var registerData: MutableLiveData<Registered> = MutableLiveData()

    fun getRegisterData(eventCode: String, registerId: String, parentRegisterId: String) = launch(IO) {

        val result = repo.getRegisterData(eventCode, registerId, parentRegisterId)

        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }

        registerData.postValue(result.data)
    }

    suspend fun getUserInfoById(userId: String): UserInfo? = withContext(IO) {
        val result = repo.getUserInfoById(userId)
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }
        return@withContext result.data
    }

    suspend fun addMemberToTeam(userToAdd: UserInfo): Boolean = withContext(IO) {

        val parentRegisterData = registerData.value?.getParentRegisterData()
                ?: RegisterData()
        val eventDetail = registerData.value?.eventDetail ?: EventDetail()

        val result = repo.addMEmberToTeam(userToAdd, parentRegisterData, eventDetail)

        when (result.isSuccessful()) {
            true -> withContext(IO) {

                //Update register data
                val updateRegisterDataResult = repo.getRegisterData(
                        eventCode = eventDetail.code ?: "",
                        registerId = parentRegisterData.id ?: "",
                        parentRegisterId = parentRegisterData.parentRegisterId ?: "")

                if (updateRegisterDataResult.isSuccessful()) {
                    registerData.postValue(updateRegisterDataResult.data)
                }
            }
            false -> onHandleError(result.statusCode, result.message)
        }

        return@withContext result.isSuccessful()
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TeamViewModel(TeamRepository(ApiService().provideService(context, EventApi::class.java))) as T
        }
    }
}