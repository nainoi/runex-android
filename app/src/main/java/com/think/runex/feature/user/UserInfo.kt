package com.think.runex.feature.user

import com.google.gson.annotations.SerializedName
import com.jozzee.android.core.datetime.dateTimeFormat
import com.think.runex.feature.event.model.Event
import com.think.runex.feature.location.Address
import com.think.runex.config.DISPLAY_DATE_FORMAT
import com.think.runex.config.DISPLAY_DATE_FORMAT_SHOT_MONTH
import com.think.runex.config.SERVER_DATE_TIME_FORMAT

data class UserInfo(
        @SerializedName("email") var email: String = "",
        @SerializedName("fullname") var fullName: String = "",
        @SerializedName("firstname") var firstName: String? = null,
        @SerializedName("firstname_th") var firstNameTh: String? = null,
        @SerializedName("lastname") var lastName: String? = null,
        @SerializedName("lastname_th") var lastNameTh: String? = null,
        @SerializedName("phone") var phone: String = "",
        @SerializedName("avatar") var avatar: String = "",
        @SerializedName("role") var role: String = "",
        @SerializedName("birthdate") var birthDate: String? = null,
        @SerializedName("gender") var gender: String = "",
        @SerializedName("created_at") var createdAt: String? = null,
        @SerializedName("updated_at") var updatedAt: String? = null,
        @SerializedName("confirm") var isConfirmed: Boolean = false,
        @SerializedName("address") var address: List<Address>? = null,
        @SerializedName("emergency_contact") var emergencyContact: String? = null,
        @SerializedName("emergency_phone") var emergencyPhone: String? = null,
        @SerializedName("nationality") var nationality: String? = null,
        @SerializedName("passport") var passport: String? = null,
        @SerializedName("citycen_id") var citizenId: String? = null,
        @SerializedName("blood_type") var bloodType: String? = null,
        @SerializedName("pf") var pf: String? = null,
        //TODO("Wait a api")
        @SerializedName("events") var events: List<Event>? = null,
        @SerializedName("strava_id") var stravaId: String? = null,
        @SerializedName("strava_avatar") var stravaAvatar: String? = null,
        @SerializedName("strava_firstname") var stravaFirstName: String? = null,
        @SerializedName("strava_latname") var stravaLatName: String? = null) {

    fun birthDate(): String = birthDate?.dateTimeFormat(SERVER_DATE_TIME_FORMAT, DISPLAY_DATE_FORMAT_SHOT_MONTH)
            ?: ""
}
