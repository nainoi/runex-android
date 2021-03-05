package com.think.runex.feature.user

import android.content.Context
import android.net.Uri
import com.think.runex.common.getDisplayName
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.RemoteDataSource
import com.think.runex.feature.user.data.UpdateProfileImageResult
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.feature.workout.data.TotalDistance
import com.think.runex.util.UploadImageUtil
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


class UserRepository(private val api: UserApi) : RemoteDataSource() {

    suspend fun userInfo(): Result<UserInfo> = call(api.getUserInfoAsync())

    suspend fun updateUserInfo(userInfo: UserInfo): Result<UserInfo> = call(api.updateUserInfoAsync(userInfo))

    suspend fun uploadProfileImage(context: Context, uri: Uri): Result<UpdateProfileImageResult> {
        val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        name = "upload",
                        filename = uri.getDisplayName(context) ?: "profile-image",
                        body = UploadImageUtil().reduceImageSize(context, uri).toRequestBody())
                .build()

        return call(api.updateProfileImageAsync(multipartBody))
    }

    suspend fun getTotalDistances(): Result<TotalDistance> = call(api.getTotalDistancesAsync())

}