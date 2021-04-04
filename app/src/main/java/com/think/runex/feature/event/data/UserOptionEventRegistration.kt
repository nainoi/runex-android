package com.think.runex.feature.event.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.think.runex.feature.address.data.SubDistrict

data class UserOptionEventRegistration(
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
        @SerializedName("home_no") var houseNo: String = "",
        @SerializedName("moo") var villageNo: String = "",
        @SerializedName("tambon") var subDistrict: SubDistrict? = null,
        @SerializedName("team") var team: String = "",
        @SerializedName("color") var color: String = "",
        @SerializedName("zone") var zone: String = "") : Parcelable {

    companion object CREATOR : Parcelable.Creator<UserOptionEventRegistration> {
        override fun createFromParcel(parcel: Parcel): UserOptionEventRegistration {
            return UserOptionEventRegistration(parcel)
        }

        override fun newArray(size: Int): Array<UserOptionEventRegistration?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readParcelable(SubDistrict::class.java.classLoader),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(fullName)
        parcel.writeString(cityCenId)
        parcel.writeString(phone)
        parcel.writeString(birthDate)
        parcel.writeString(gender)
        parcel.writeString(bloodType)
        parcel.writeString(emergencyContact)
        parcel.writeString(emergencyPhone)
        parcel.writeString(address)
        parcel.writeString(houseNo)
        parcel.writeString(villageNo)
        parcel.writeParcelable(subDistrict, flags)
        parcel.writeString(team)
        parcel.writeString(color)
        parcel.writeString(zone)
    }

    override fun describeContents(): Int {
        return 0
    }

}