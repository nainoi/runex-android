package com.think.runex.feature.user

import com.google.gson.annotations.SerializedName
import com.think.runex.feature.location.Address

data class Profile(
        @SerializedName("email") var email: String = "",
        @SerializedName("provider") var provider: String = "",
        @SerializedName("provider_id") var providerId: String = "",
        @SerializedName("fullname") var fullName: String = "",
        @SerializedName("firstname") var firstName: String = "",
        @SerializedName("lastname") var lastName: String = "",
        @SerializedName("phone") var phone: String = "",
        @SerializedName("avatar") var avatar: String = "",
        @SerializedName("role") var role: String = "",
        @SerializedName("birthdate") var birthDate: String = "",
        @SerializedName("gender") var gender: String = "",
        @SerializedName("created_at") var createdAt: String = "",
        @SerializedName("updated_at") var updatedAt: String = "",
        @SerializedName("confirm") var isConfirmed: Boolean = false,
        @SerializedName("address") var address: List<Address>? = null)
