package com.think.runex.feature.event.data.request

import com.google.gson.annotations.SerializedName
import com.think.runex.feature.address.data.SubDistrict

data class UserEventRegistrationBody(
        @SerializedName("firstname") var firstName: String = "",
        @SerializedName("lastname") var lastName: String = "",
        //@SerializedName("firstname_th") var firstNameTh: String = "",
        //@SerializedName("lastname_th") var lastNameTh: String = "",
        @SerializedName("fullname") var fullName: String = "",
        @SerializedName("citycen_id") var cityCenId: String = "",
        @SerializedName("phone") var phone: String = "",
        @SerializedName("birthdate") var birthDate: String = "",
        @SerializedName("gender") var gender: String = "",
        @SerializedName("blood_type") var bloodType: String = "",
        @SerializedName("emergency_contact") var emergencyContact: String = "",
        @SerializedName("emergency_phone") var emergencyPhone: String = "",
        @SerializedName("address") var address: String = "",
        @SerializedName("tambon") var subDistrict: SubDistrict? = null)