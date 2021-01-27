package com.think.runex.feature.event.model

import com.google.gson.annotations.SerializedName
import com.jozzee.android.core.datetime.dateTimeFormat
import com.think.runex.config.SERVER_DATE_TIME_FORMAT
import com.think.runex.datasource.api.ApiConfig

data class EventDetail(
        @SerializedName("agreement") var agreement: String? = "",
        @SerializedName("category") var category: String? = "",
        @SerializedName("code") var code: String? = "",
        @SerializedName("contact") var contact: String? = null,
        @SerializedName("contactFacebook") var contactFacebook: String? = null,
        @SerializedName("contactLine") var contactLine: String? = null,
        @SerializedName("content") var content: String? = "",
        @SerializedName("cover") var coverImage: String? = "",
        @SerializedName("coverThumbnail") var coverThumbnail: String? = "",
        @SerializedName("eventDate") var eventDateDisplay: String? = "",
        @SerializedName("eventEndDate") var eventEndDate: String? = "",
        @SerializedName("eventEndDateText") var eventEndDateDisplay: String? = "",
        @SerializedName("eventStartDate") var eventStartDate: String? = "",
        @SerializedName("eventStartDateText") var eventStartDateDisplay: String? = "",
        @SerializedName("id") var id: Int? = 0,
        @SerializedName("isFreeEvent") var isFreeEvent: Boolean? = false,
        @SerializedName("isRunexOnly") var isRunexOnly: Boolean? = false,
        @SerializedName("isSendShirtByPost") var isSendShirtByPost: Boolean? = false,
        @SerializedName("organizer") var organizer: String? = "",
        @SerializedName("photoBib") var photoBib: String? = null,
        @SerializedName("photoBibThumbnail") var photoBibThumbnail: String? = null,
        @SerializedName("photoCert") var photoCert: String? = null,
        @SerializedName("photoCertThumbnail") var photoCertThumbnail: String? = null,
        @SerializedName("photoMedal") var photoMedal: String? = null,
        @SerializedName("photoMedalThumbnail") var photoMedalThumbnail: String? = null,
        @SerializedName("photoShirt") var photoShirt: String? = null,
        @SerializedName("photoShirtThumbnail") var photoShirtThumbnail: String? = null,
        @SerializedName("place") var place: String? = "",
        @SerializedName("prizes") var prizes: List<Prize>? = null,
        @SerializedName("registerEndDate") var registerEndDate: String? = "",
        @SerializedName("registerEndDateText") var registerEndDateDisplay: String? = "",
        @SerializedName("registerStartDate") var registerStartDate: String? = "",
        @SerializedName("registerStartDateText") var registerStartDateDisplay: String? = "",
        @SerializedName("schedules") var schedules: List<Schedule>? = null,
        @SerializedName("shirts") var shirts: List<Shirts>? = null,
        @SerializedName("title") var title: String? = "",
        @SerializedName("userId") var userId: String? = "") {


    fun coverImage(): String {
        return when (coverImage?.startsWith("http", false) == true) {
            true -> coverImage ?: ""
            false -> ("${ApiConfig.BASE_URL}${coverImage ?: ""}")
        }
    }

    fun eventPeriodWithTime(): String {
        val dateTimeFormat = "dd MMM yyyy(HH:mm)"
        val startEventDate = eventStartDate?.dateTimeFormat(SERVER_DATE_TIME_FORMAT, dateTimeFormat)
                ?: ""
        val endEventDate = eventEndDate?.dateTimeFormat(SERVER_DATE_TIME_FORMAT, dateTimeFormat)
                ?: ""
        return "$startEventDate - $endEventDate"
    }
}