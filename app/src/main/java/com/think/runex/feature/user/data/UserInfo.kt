package com.think.runex.feature.user.data

import com.google.gson.annotations.SerializedName
import com.jozzee.android.core.datetime.dateTimeFormat
import com.jozzee.android.core.datetime.toCalendar
import com.think.runex.common.displayFormat
import com.think.runex.feature.event.data.EventItem
import com.think.runex.feature.address.data.Address
import com.think.runex.config.DISPLAY_DATE_FORMAT_SHOT_MONTH
import com.think.runex.config.SERVER_DATE_TIME_FORMAT
import java.util.*

data class UserInfo(
        @SerializedName("email") var email: String? = null,
        @SerializedName("provider") var provider:String? = null,
        @SerializedName("provider_id") var providerId:String? = null,
        @SerializedName("fullname") var fullName: String? = null,
        @SerializedName("firstname") var firstName: String? = null,
        @SerializedName("firstname_th") var firstNameTh: String? = null,
        @SerializedName("lastname") var lastName: String? = null,
        @SerializedName("lastname_th") var lastNameTh: String? = null,
        @SerializedName("phone") var phone: String? = null,
        @SerializedName("avatar") var avatar: String? = null,
        @SerializedName("role") var role: String? = null,
        @SerializedName("birthdate") var birthDate: String? = null,
        @SerializedName("gender") var gender: String? = null,
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
        @SerializedName("events") var events: List<EventItem>? = null,
        @SerializedName("strava_id") var stravaId: String? = null,
        @SerializedName("strava_avatar") var stravaAvatar: String? = null,
        @SerializedName("strava_firstname") var stravaFirstName: String? = null,
        @SerializedName("strava_latname") var stravaLatName: String? = null) {

    constructor(userInfo: UserInfo?) : this(
            userInfo?.email,
            userInfo?.provider,
            userInfo?.providerId,
            userInfo?.fullName,
            userInfo?.firstName,
            userInfo?.firstNameTh,
            userInfo?.lastName,
            userInfo?.lastNameTh,
            userInfo?.phone,
            userInfo?.avatar,
            userInfo?.role,
            userInfo?.birthDate,
            userInfo?.gender,
            userInfo?.createdAt,
            userInfo?.updatedAt,
            userInfo?.isConfirmed ?: false,
            userInfo?.address,
            userInfo?.emergencyContact,
            userInfo?.emergencyPhone,
            userInfo?.nationality,
            userInfo?.passport,
            userInfo?.citizenId,
            userInfo?.bloodType,
            userInfo?.pf,
            userInfo?.events,
            userInfo?.stravaId,
            userInfo?.stravaAvatar,
            userInfo?.stravaFirstName,
            userInfo?.stravaLatName)

    var totalDistance: Double? = 0.0

    @JvmName("getBirthDateDisplay")
    fun getBirthDate(pattern: String = DISPLAY_DATE_FORMAT_SHOT_MONTH): String {
        return birthDate?.dateTimeFormat(SERVER_DATE_TIME_FORMAT, pattern) ?: ""
    }

    fun getBirthDateCalendar(): Calendar? = birthDate?.toCalendar(SERVER_DATE_TIME_FORMAT)

    fun getTotalDistance(unit: String): String {
        return ("${totalDistance?.displayFormat() ?: ""} $unit")
    }
}
