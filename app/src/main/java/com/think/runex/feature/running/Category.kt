package com.think.runex.feature.running

import com.google.gson.annotations.SerializedName

data class Category(
        @SerializedName("id") var id: String = "",
        @SerializedName("name") var name: String = "",
        @SerializedName("description") var description: String = "",
        @SerializedName("active") var isActive: Boolean = false,
        @SerializedName("created_at") var createdAt: String = "",
        @SerializedName("updated_at") var updatedAt: String = "")