package com.think.runex.feature.event.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.jozzee.android.core.datetime.dateTimeFormat
import com.jozzee.android.core.datetime.toTimeMillis
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
        @SerializedName("isClosed") var isClosed: Boolean? = null,
        @SerializedName("isFreeEvent") var isFreeEvent: Boolean? = false,
        @SerializedName("isRunexOnly") var isRunexOnly: Boolean? = false,
        @SerializedName("isSendShirtByPost") var isSendShirtByPost: Boolean? = false,
        @SerializedName("isOpenPayment") var isOpenPayment: Boolean? = false,
        @SerializedName("isOpenRegister") var isOpenRegister: Boolean? = false,
        @SerializedName("isOpenSendActivity") var isOpenSendActivity: Boolean? = false,
        @SerializedName("isPublish") var isPublish: Boolean? = false,
        @SerializedName("isTransactionFixedRate") val isTransactionFixedRate: Boolean? = false,
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
        @SerializedName("slug") var slug: String? = null,
        @SerializedName("tickets") var tickets: List<Ticket>? = null,
        @SerializedName("shirts") var shirts: List<Shirt>? = null,
        @SerializedName("title") var title: String? = "",
        @SerializedName("userId") var ownerId: String? = "",
        @SerializedName("pictures") var pictures: List<Picture>? = null) : Parcelable {

    companion object CREATOR : Parcelable.Creator<EventDetail> {
        override fun createFromParcel(parcel: Parcel): EventDetail {
            return EventDetail(parcel)
        }

        override fun newArray(size: Int): Array<EventDetail?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(Prize),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(Schedule),
            parcel.readString(),
            parcel.createTypedArrayList(Ticket),
            parcel.createTypedArrayList(Shirt),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(Picture))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(agreement)
        parcel.writeString(category)
        parcel.writeString(code)
        parcel.writeString(contact)
        parcel.writeString(contactFacebook)
        parcel.writeString(contactLine)
        parcel.writeString(content)
        parcel.writeString(coverImage)
        parcel.writeString(coverThumbnail)
        parcel.writeString(eventDateDisplay)
        parcel.writeString(eventEndDate)
        parcel.writeString(eventEndDateDisplay)
        parcel.writeString(eventStartDate)
        parcel.writeString(eventStartDateDisplay)
        parcel.writeValue(id)
        parcel.writeValue(isClosed)
        parcel.writeValue(isFreeEvent)
        parcel.writeValue(isRunexOnly)
        parcel.writeValue(isSendShirtByPost)
        parcel.writeValue(isOpenPayment)
        parcel.writeValue(isOpenRegister)
        parcel.writeValue(isOpenSendActivity)
        parcel.writeValue(isPublish)
        parcel.writeValue(isTransactionFixedRate)
        parcel.writeString(organizer)
        parcel.writeString(photoBib)
        parcel.writeString(photoBibThumbnail)
        parcel.writeString(photoCert)
        parcel.writeString(photoCertThumbnail)
        parcel.writeString(photoMedal)
        parcel.writeString(photoMedalThumbnail)
        parcel.writeString(photoShirt)
        parcel.writeString(photoShirtThumbnail)
        parcel.writeString(place)
        parcel.writeTypedList(prizes)
        parcel.writeString(registerEndDate)
        parcel.writeString(registerEndDateDisplay)
        parcel.writeString(registerStartDate)
        parcel.writeString(registerStartDateDisplay)
        parcel.writeTypedList(schedules)
        parcel.writeString(slug)
        parcel.writeTypedList(tickets)
        parcel.writeTypedList(shirts)
        parcel.writeString(title)
        parcel.writeString(ownerId)
        parcel.writeTypedList(pictures)
    }

    override fun describeContents(): Int {
        return 0
    }

    @JvmName("getCoverImageJava")
    fun getCoverImage(): String {
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

    fun isActive(): Boolean {
        val now = System.currentTimeMillis()
        val start = eventStartDate?.toTimeMillis(SERVER_DATE_TIME_FORMAT) ?: 0
        val end = eventEndDate?.toTimeMillis(SERVER_DATE_TIME_FORMAT) ?: 0
        return now in start..end
    }
}