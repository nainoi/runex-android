package com.think.runex.feature.user

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.think.runex.datasource.BaseViewModel
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.util.launchIoThread
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class UserViewModel(private val repo: UserRepository) : BaseViewModel() {

    val userInfo: MutableLiveData<UserInfo> by lazy {
        MutableLiveData<UserInfo>()
    }

    fun getUSerInfo() = launchIoThread {
        val userInfoResult = repo.userInfo()
        if (userInfoResult.isSuccessful()) {
            val totalDistanceResult = repo.getTotalDistances()
            userInfoResult.data?.totalDistance = totalDistanceResult.data?.totalDistance
            userInfo.postValue(userInfoResult.data)
        } else {
            onHandleError(userInfoResult.statusCode, userInfoResult.message)
        }
    }

    suspend fun updateUserInfo(userInfo: UserInfo): Boolean = withContext(IO) {
        val result = repo.updateUserInfo(userInfo)
        when (result.isSuccessful()) {
            true -> this@UserViewModel.userInfo.postValue(result.data)
            false -> onHandleError(result.statusCode, result.message)
        }
        return@withContext result.isSuccessful()
    }

    suspend fun updateProfileImage(context: Context, uri: Uri): Boolean = withContext(IO) {

        //Upload profile image
        val uploadImageResult = repo.uploadProfileImage(context, uri)
        if (uploadImageResult.isSuccessful().not()) {
            onHandleError(uploadImageResult.statusCode, uploadImageResult.message)
            return@withContext false
        }

        if (userInfo.value == null) {
            return@withContext false
        }

        //Update user info (update avatar)
        val userInfoUpdate = UserInfo(userInfo.value)
        userInfoUpdate.avatar = uploadImageResult.data?.url
        val userInfoUpdateResult = repo.updateUserInfo(userInfoUpdate)
        if (userInfoUpdateResult.isSuccessful().not()) {
            onHandleError(userInfoUpdateResult.statusCode, userInfoUpdateResult.message)
            return@withContext false
        }
        userInfo.postValue(userInfoUpdateResult.data)
        return@withContext userInfoUpdateResult.isSuccessful()
    }

}