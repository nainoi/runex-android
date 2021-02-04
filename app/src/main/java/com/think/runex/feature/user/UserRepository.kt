package com.think.runex.feature.user

import android.content.Context
import android.net.Uri
import com.think.runex.common.getDisplayName
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.workout.model.TotalDistanceResponse
import com.think.runex.util.ImageUtil
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


class UserRepository(private val api: UserApi) : RemoteDataSource() {

    suspend fun userInfo(): Result<UserInfo> = call(api.getUserInfoAsync())

    suspend fun updateUserInfo(userInfo: UserInfo): Result<UserInfo> = call(api.updateUserInfoAsync(userInfo))

    suspend fun uploadProfileImage(context: Context, uri: Uri): Result<UpdateProfileImageResponse> {
        val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        name = "upload",
                        filename = uri.getDisplayName(context) ?: "profile-image",
                        body = ImageUtil().reduceImageSize(context, uri).toRequestBody())
                .build()

        return call(api.updateProfileImageAsync(multipartBody))
    }

    suspend fun getTotalDistances(): Result<TotalDistanceResponse> = call(api.getTotalDistancesAsync())

}