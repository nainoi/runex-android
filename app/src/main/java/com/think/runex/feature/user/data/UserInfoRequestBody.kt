package com.think.runex.feature.user.data

import com.google.gson.annotations.SerializedName

data class UserInfoRequestBody(@SerializedName("uid") var userId: String)