package com.think.runex.feature.event.model

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.jozzee.android.core.datetime.dateTimeFormat
import com.think.runex.R
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.product.Product
import com.think.runex.feature.ticket.Ticket
import com.think.runex.util.DISPLAY_DATE_FORMAT_SHOT_MONTH
import com.think.runex.util.SERVER_DATE_TIME_FORMAT

data class Event(
        @SerializedName("id") var id: String = "",
        @SerializedName("name") var name: String = "",
        @SerializedName("description") var description: String = "",
        @SerializedName("body") var body: String = "",
        @SerializedName("cover") var coverImage: String = "",
        @SerializedName("cover_thumb") var coverThumbnailImages: List<EventCoverThumbnailImage>? = null,
        @SerializedName("category") var category: String? = null,
        @SerializedName("slug") var slug: String? = null,
        @SerializedName("ticket") var ticket: List<Ticket>? = null,
        @SerializedName("owner_id") var ownerId: String? = null,
        @SerializedName("status") var status: String? = null,
        @SerializedName("location") var location: String? = null,
        @SerializedName("receive_location") var receiveLocation: String? = null,
        @SerializedName("is_active") var isActive: Boolean = false,
        @SerializedName("is_free") var isFree: Boolean = false,
        @SerializedName("start_reg") var startRegisterDate: String = "",
        @SerializedName("end_reg") var endRegisterDate: String = "",
        @SerializedName("start_event") var startEventDate: String = "",
        @SerializedName("end_event") var endEventDate: String = "",
        @SerializedName("inapp") var isInApp: Boolean = false,
        @SerializedName("is_post") var isPost: Boolean = false,
        @SerializedName("post_end_date") var postEndDate: String = "",
        @SerializedName("partner") var partner: Partner? = null,
        @SerializedName("created_time") var createdAt: String = "",
        @SerializedName("updated_time") var updatedAt: String = "") {


    fun coverImage(): String = if (coverImage.isNotBlank()) "${ApiConfig.BASE_URL}$coverImage" else ""

    fun eventPeriod(context: Context): String {
        return "${context.getString(R.string.event_date)} " +
                "${startEventDate.dateTimeFormat(SERVER_DATE_TIME_FORMAT, DISPLAY_DATE_FORMAT_SHOT_MONTH)} - " +
                endEventDate.dateTimeFormat(SERVER_DATE_TIME_FORMAT, DISPLAY_DATE_FORMAT_SHOT_MONTH)
    }

    fun registerPeriod(context: Context): String {
        return "${context.getString(R.string.register_date)} " +
                "${startRegisterDate.dateTimeFormat(SERVER_DATE_TIME_FORMAT, DISPLAY_DATE_FORMAT_SHOT_MONTH)} - " +
                endRegisterDate.dateTimeFormat(SERVER_DATE_TIME_FORMAT, DISPLAY_DATE_FORMAT_SHOT_MONTH)
    }
}