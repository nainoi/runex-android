package com.think.runex.feature.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.think.runex.datasource.BaseViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val repo: UserRepository) : BaseViewModel() {


    suspend fun getUSerInfo(): UserInfo? = withContext(IO) {
        val userInfoResult = repo.userInfo()
        if (userInfoResult.isSuccessful()) {
            val totalDistanceResult = repo.getTotalDistances()
            userInfoResult.data?.totalDistance = totalDistanceResult.data?.totalDistance
        } else {
            onHandleError(userInfoResult.statusCode, userInfoResult.message)
        }
        return@withContext userInfoResult.data
    }
}