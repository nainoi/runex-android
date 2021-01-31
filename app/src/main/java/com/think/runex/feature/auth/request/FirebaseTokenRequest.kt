package com.think.runex.feature.auth.request

import com.google.gson.annotations.SerializedName

data class FirebaseTokenRequest(
        @SerializedName("firebase_token") var firebaseToken: String? = "")