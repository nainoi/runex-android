package com.think.runex.feature.user.data

import com.google.gson.annotations.SerializedName

data class UpdateProfileImageResult(
        @SerializedName("url") var url: String? = null,
        @SerializedName("file_name") var fileName: String? = null,
        @SerializedName("type") var type: String? = null)