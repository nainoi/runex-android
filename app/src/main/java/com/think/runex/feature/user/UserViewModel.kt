package com.think.runex.feature.user

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.think.runex.base.BaseViewModel
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.util.UploadImageUtil
import com.think.runex.util.extension.getDisplayName
import com.think.runex.util.extension.launch
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserViewModel(private val repo: UserRepository) : BaseViewModel() {

    val userInfo: MutableLiveData<UserInfo> by lazy {
        MutableLiveData<UserInfo>()
    }

    fun getUSerInfo() = launch(IO) {

        val userInfoResult = repo.userInfo()

        if (userInfoResult.isSuccessful()) {

            val totalDistanceResult = repo.getTotalDistances()
            userInfoResult.data?.totalDistance = totalDistanceResult.data?.totalDistance

            userInfo.postValue(userInfoResult.data)

        } else {
            onHandleError(userInfoResult.code, userInfoResult.message)
        }
    }

    suspend fun getUSerInfoInstance(): UserInfo? = withContext(IO) {

        if (userInfo.value != null) {
            return@withContext userInfo.value
        }

        val userInfoResult = repo.userInfo()

        if (userInfoResult.isSuccessful()) {

            val totalDistanceResult = repo.getTotalDistances()
            userInfoResult.data?.totalDistance = totalDistanceResult.data?.totalDistance

            userInfo.postValue(userInfoResult.data)

        } else {
            onHandleError(userInfoResult.code, userInfoResult.message)
        }

        return@withContext userInfoResult.data
    }

    fun getUserInfoIfNotHave() {
        if (userInfo.value == null) {
            getUSerInfo()
        }
    }

    suspend fun updateUserInfo(userInfo: UserInfo): Boolean = withContext(IO) {

        val result = repo.updateUserInfo(userInfo)

        if (result.isSuccessful()) {

            val totalDistanceResult = repo.getTotalDistances()

            if (totalDistanceResult.isSuccessful()) {
                result.data?.totalDistance = totalDistanceResult.data?.totalDistance
                        ?: result.data?.totalDistance
            }
            this@UserViewModel.userInfo.postValue(result.data)

        } else {
            onHandleError(result.code, result.message)
        }

        return@withContext result.isSuccessful()
    }

    suspend fun updateProfileImage(context: Context, imageUri: Uri): Boolean = withContext(IO) {

        //Upload new profile image to server.
        val body = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        name = "upload",
                        filename = imageUri.getDisplayName(context) ?: "profile-image",
                        body = UploadImageUtil().reduceImageSize(context, imageUri).toRequestBody())
                .build()

        val uploadImageResult = repo.uploadProfileImage(body)

        if (uploadImageResult.isSuccessful().not()) {
            onHandleError(uploadImageResult.code, uploadImageResult.message)
            return@withContext false
        }

        if (userInfo.value == null) {
            return@withContext false
        }

        //Update user info (update avatar) after upload new profile image.
        val userInfoUpdate = UserInfo(userInfo.value)
        userInfoUpdate.avatar = uploadImageResult.data?.url

        val userInfoUpdateResult = repo.updateUserInfo(userInfoUpdate)

        if (userInfoUpdateResult.isSuccessful().not()) {
            onHandleError(userInfoUpdateResult.code, userInfoUpdateResult.message)
            return@withContext false
        }

        //Update total distance of user.
        val totalDistanceResult = repo.getTotalDistances()
        if (totalDistanceResult.isSuccessful()) {
            userInfoUpdateResult.data?.totalDistance = totalDistanceResult.data?.totalDistance
                    ?: userInfo.value?.totalDistance
        }

        userInfo.postValue(userInfoUpdateResult.data)
        return@withContext userInfoUpdateResult.isSuccessful()
    }


    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserViewModel(UserRepository(ApiService().provideService(context, UserApi::class.java))) as T
        }
    }
}