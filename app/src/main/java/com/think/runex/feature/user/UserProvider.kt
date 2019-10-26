package com.think.runex.feature.user

import com.google.gson.annotations.SerializedName

/**
 * Profile data with given social provider
 */
data class UserProvider(
        @SerializedName("id") var id: String = "",
        @SerializedName("fullname") var fullName: String = "",
        @SerializedName("firstName") var firstName: String = "",
        @SerializedName("lastName") var lastName: String = "",
        @SerializedName("email") var email: String = "",
        @SerializedName("phone") var phone: String = "",
        @SerializedName("avatar") var avatar: String = "")