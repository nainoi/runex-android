package com.think.runex.feature.user

import com.google.gson.annotations.SerializedName

data class UpdateProfileImageResponse(
        @SerializedName("url") var url: String? = null,
        @SerializedName("file_name") var fileName: String? = null,
        @SerializedName("type") var type: String? = null)