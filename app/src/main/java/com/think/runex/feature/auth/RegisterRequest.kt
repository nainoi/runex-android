package com.think.runex.feature.auth

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
        @SerializedName("email") val email: String,
        @SerializedName("password") val password: String,
        @SerializedName("firstname") val firstName: String,
        @SerializedName("lastname") val lastName: String,
        @SerializedName("role") val role: String,
        @SerializedName("gender") val gender: String,
        @SerializedName("birthdate") val birthDate: String,
        @SerializedName("PF") val platform: String)