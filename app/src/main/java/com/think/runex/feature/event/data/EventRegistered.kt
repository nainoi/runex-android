package com.think.runex.feature.event.data

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.jozzee.android.core.datetime.dateTimeFormat
import com.think.runex.R
import com.think.runex.config.DISPLAY_DATE_FORMAT_SHOT_MONTH
import com.think.runex.config.SERVER_DATE_TIME_FORMAT

data class EventRegistered(
        @SerializedName("body") var body: String? = "",
        @SerializedName("category") var category: String? = "",
        @SerializedName("cover") var coverImage: String? = "",
        @SerializedName("cover_thumb") var coverThumbnailImages: List<CoverThumbnailImage>? = null,
        @SerializedName("created_time") var createdTime: String? = "",
        @SerializedName("description") var description: String? = "",
        @SerializedName("end_event") var eventEndDate: String? = "",
        @SerializedName("end_reg") var registerEndDate: String? = "",
        @SerializedName("id") var id: String? = "",
        @SerializedName("inapp") var isInApp: Boolean? = false,
        @SerializedName("is_active") var isActive: Boolean? = false,
        @SerializedName("is_free") var isFreeEvent: Boolean? = false,
        @SerializedName("is_post") var isPost: Boolean? = false,
        @SerializedName("location") var location: String? = null,
        @SerializedName("name") var name: String? = null,
        @SerializedName("owner_id") var ownerId: String? = null,
        @SerializedName("partner") var partner: Partner? = null,
        @SerializedName("post_end_date") var postEndDate: String? = null,
        @SerializedName("receive_location") var receiveLocation: String? = null,
        @SerializedName("slug") var slug: String? = null,
        @SerializedName("start_event") var eventStartDate: String? = null,
        @SerializedName("start_reg") var registerStartDate: String? = null,
        @SerializedName("status") var status: String? = null,
        @SerializedName("ticket") var ticket: List<TicketEventRegistered>? = null,
        @SerializedName("updated_time") var updatedAt: String? = null) {

    var isChecked: Boolean = false

    fun registerEventPeriod(context: Context): String {
        val startEventDate = eventStartDate?.dateTimeFormat(SERVER_DATE_TIME_FORMAT, DISPLAY_DATE_FORMAT_SHOT_MONTH)
                ?: ""
        val endEventDate = eventEndDate?.dateTimeFormat(SERVER_DATE_TIME_FORMAT, DISPLAY_DATE_FORMAT_SHOT_MONTH)
                ?: ""
        return "${context.getString(R.string.register_date)} $startEventDate - $endEventDate"
    }
}