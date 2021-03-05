package com.think.runex.feature.auth.data.request

import com.google.gson.annotations.SerializedName

data class FirebaseTokenBody(
        @SerializedName("firebase_token") var firebaseToken: String? = "")