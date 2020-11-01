package com.think.runex.feature.event.model

import com.google.gson.annotations.SerializedName

data class EventCoverThumbnailImage(
        @SerializedName("image") var image: String? = null,
        @SerializedName("size") var size: String? = null)