package com.think.runex.feature.event.registration

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jozzee.android.core.datetime.dateTimeFormat
import com.jozzee.android.core.text.toDoubleOrZero
import com.jozzee.android.core.util.Logger
import com.think.runex.config.SERVER_DATE_TIME_FORMAT
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.address.AddressApi
import com.think.runex.feature.address.AddressRepository
import com.think.runex.feature.address.data.AddressAutoFill
import com.think.runex.feature.address.data.SubDistrict
import com.think.runex.feature.event.EventApi
import com.think.runex.feature.event.EventRepository
import com.think.runex.feature.event.data.*
import com.think.runex.feature.event.data.TicketOptionEventRegistration
import com.think.runex.feature.event.data.UserOptionEventRegistration
import com.think.runex.feature.event.detail.EventDetailsViewModel
import com.think.runex.util.extension.launch
import com.think.runex.util.extension.toJson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class RegistrationViewModel(
    eventRepo: EventRepository,
    private val addressRepo: AddressRepository
) : EventDetailsViewModel(eventRepo) {

    private var allSubDistrictList: List<SubDistrict>? = null
    private var firstThreeLettersQuery: String = ""

    var registerStatus: String = RegisterStatus.REGISTER

    val updateScreen: MutableLiveData<String> by lazy { MutableLiveData() }

    val addressAutoFill: MutableLiveData<AddressAutoFill> by lazy { MutableLiveData() }

    private var currentNo: Int = 1

    private var ticketOptions: ArrayList<TicketOptionEventRegistration> = ArrayList()

    private var subDistricts: ArrayList<SubDistrict> = ArrayList()

    private var registerId: String? = null


    fun onGetEventDetailCompleted() {
        updateScreen.postValue(ChooseTicketFragment::class.java.simpleName)
    }

    fun getCurrentTicketOption(): TicketOptionEventRegistration? {
        if (ticketOptions.isEmpty()) {
            ticketOptions.add(TicketOptionEventRegistration())
        }
        if (currentNo <= ticketOptions.size) {
            return ticketOptions[currentNo - 1]
        }
        return null
    }

    fun getCurrentSubDistrict(): SubDistrict? {
        if (currentNo <= subDistricts.size) {
            return subDistricts[currentNo - 1]
        }
        return null
    }

    fun onSelectTicket(ticket: Ticket) {
        getCurrentTicketOption()?.apply {
            this.ticket = ticket
            this.totalPrice = ticket.price?.toDoubleOrZero() ?: 0.0
            updateScreen.postValue(FillOutUserInfoFragment::class.java.simpleName)
        }
    }

    fun onSelectShirt(shirt: Shirt) {
        getCurrentTicketOption()?.apply {
            this.shirt = shirt
        }
    }

    fun setUserData(userOption: UserOptionEventRegistration) {
        getCurrentTicketOption()?.apply {
            this.userOption = userOption
            updateScreen.postValue(ConfirmRegistrationFragment::class.java.simpleName)
        }
    }

    fun initialSubDistrict(subDistrict: SubDistrict) {
        when (currentNo > subDistricts.size) {
            true -> subDistricts.add(subDistrict)
            false -> subDistricts[(currentNo - 1)] = subDistrict
        }
    }

    fun searchAddressByZipCode(zipCode: String, viewRequestId: Int) = launch(IO) {
        Logger.debug("Jozzee", "searchAddressByZipCode: $zipCode")
        val result = addressRepo.getSubDistrictByZipCode(zipCode)
        when (result.isSuccessful()) {
            true -> {
                firstThreeLettersQuery = ""
                allSubDistrictList = result.data

                val autoFillList = allSubDistrictList?.map { it.getFullAddress() } ?: emptyList()
                addressAutoFill.postValue(AddressAutoFill(viewRequestId, autoFillList.toTypedArray()))
            }
            false -> onHandleError(result.code, result.message, "Address")
        }
    }

    fun searchAddressBySubDistricts(query: String, viewRequestId: Int) = launch(IO) {
        Logger.debug("Jozzee", "searchAddressBySubDistricts: $query")
        if (addressAutoFill.value?.viewRequestId == viewRequestId && query.startsWith(firstThreeLettersQuery)) {
            val autoFillList = ArrayList<String>()
            allSubDistrictList?.forEach { address ->
                if (address.subDistrict?.lowercase(Locale.getDefault())
                        ?.contains(query.lowercase(Locale.getDefault())) == true
                ) {
                    autoFillList.add(address.getFullAddress())
                }
            }
            addressAutoFill.postValue(AddressAutoFill(viewRequestId, autoFillList.toTypedArray()))
            return@launch
        }

        val result = addressRepo.getSubDistrictBySubDistrict(query)
        when (result.isSuccessful()) {
            true -> {
                firstThreeLettersQuery = query
                allSubDistrictList = result.data

                val autoFillList = allSubDistrictList?.map { it.getFullAddress() } ?: emptyList()
                addressAutoFill.postValue(AddressAutoFill(viewRequestId, autoFillList.toTypedArray()))
            }
            false -> onHandleError(result.code, result.message, "Address")
        }
    }

    fun searchAddressByDistricts(query: String, viewRequestId: Int) = launch(IO) {
        Logger.debug("Jozzee", "searchAddressByDistricts: $query")
        if (addressAutoFill.value?.viewRequestId == viewRequestId && query.startsWith(firstThreeLettersQuery)) {
            val autoFillList = ArrayList<String>()
            allSubDistrictList?.forEach { address ->
                if (address.district?.lowercase(Locale.getDefault())
                        ?.contains(query.lowercase(Locale.getDefault())) == true
                ) {
                    autoFillList.add(address.getFullAddress())
                }
            }
            addressAutoFill.postValue(AddressAutoFill(viewRequestId, autoFillList.toTypedArray()))
            return@launch
        }

        val result = addressRepo.getSubDistrictByDistrict(query)
        when (result.isSuccessful()) {
            true -> {
                firstThreeLettersQuery = query
                allSubDistrictList = result.data

                val autoFillList = allSubDistrictList?.map { it.getFullAddress() } ?: emptyList()
                addressAutoFill.postValue(AddressAutoFill(viewRequestId, autoFillList.toTypedArray()))
            }
            false -> onHandleError(result.code, result.message, "Address")
        }
    }

    fun searchAddressByProvince(query: String, viewRequestId: Int) = launch(IO) {
        Logger.debug("Jozzee", "searchAddressByProvince: $query")
        if (addressAutoFill.value?.viewRequestId == viewRequestId && query.startsWith(firstThreeLettersQuery)) {
            val autoFillList = ArrayList<String>()
            allSubDistrictList?.forEach { address ->
                if (address.province?.lowercase(Locale.getDefault())
                        ?.contains(query.lowercase(Locale.getDefault())) == true
                ) {
                    autoFillList.add(address.getFullAddress())
                }
            }
            addressAutoFill.postValue(AddressAutoFill(viewRequestId, autoFillList.toTypedArray()))
            return@launch
        }

        val result = addressRepo.getSubDistrictByProvince(query)
        when (result.isSuccessful()) {
            true -> {
                firstThreeLettersQuery = query
                allSubDistrictList = result.data

                val autoFillList = allSubDistrictList?.map { it.getFullAddress() } ?: emptyList()
                addressAutoFill.postValue(AddressAutoFill(viewRequestId, autoFillList.toTypedArray()))
            }
            false -> onHandleError(result.code, result.message, "Address")
        }
    }

    fun getSubDistrictFromFullAddress(fullAddress: String): SubDistrict? {
        allSubDistrictList?.firstOrNull { it.getFullAddress() == fullAddress }?.also { subDistrict ->
            when (currentNo > subDistricts.size) {
                true -> subDistricts.add(subDistrict)
                false -> subDistricts[(currentNo - 1)] = subDistrict
            }
        }
        return getCurrentSubDistrict()
    }

    suspend fun registerEvent(): Registered? = withContext(IO) {

        ticketOptions.forEach { ticketOption ->
            ticketOption.receiptType = when (eventDetail.value?.category) {
                EventCategory.VIRTUAL_RUN -> "myself"
                else -> ""
            }
        }

        val gSon = Gson()

        val registerObject = JsonObject().apply {
            add("event", gSon.toJsonTree(eventDetail.value))
            addProperty("status", RegisterStatus.WAITING_PAY)
            addProperty("payment_type", "")
            addProperty("total_price", ticketOptions.sumByDouble { it.totalPrice })
            addProperty("discount_price", 0.0)
            addProperty("promo_code", "")
            addProperty("reg_date", System.currentTimeMillis().dateTimeFormat(SERVER_DATE_TIME_FORMAT))
            addProperty("ticket_id", getCurrentTicketOption()?.ticket?.id ?: "")
            add("ticket_options", gSon.toJsonTree(ticketOptions))
        }

        val body = JsonObject().apply {
            addProperty("event_id", eventDetail.value?.id ?: 0)
            addProperty("event_code", eventDetail.value?.code ?: "")
            addProperty("owner_id", eventDetail.value?.ownerId ?: "")
            add("regs", gSon.toJsonTree(registerObject))
        }

        val result = repo.registerEvent(body)

        if (result.isSuccessful().not()) {
            onHandleError(result.code, result.message)
        }

        return@withContext result.data
    }

    fun updateRegisterData(registered: Registered) {

        //Update register status
        registerStatus = registered.getRegisterStatus(0) ?: registerStatus

        registerId = registered.registeredDataList?.get(0)?.id

        //Update ticket options
        val ticketOption: TicketOptionEventRegistration? = registered.registeredDataList?.get(0)?.ticketOptions?.get(0)

        getCurrentTicketOption()?.apply {
            receiptType = ticketOption?.receiptType ?: ""
            registerNumber = ticketOption?.registerNumber
            shirt = ticketOption?.shirt
            ticket = ticketOption?.ticket
            totalPrice = ticketOption?.totalPrice ?: 0.0
            userOption = ticketOption?.userOption
        }

        this.eventDetail.postValue(registered.eventDetail)
    }

    suspend fun updateRegisterInfo(): Boolean = withContext(IO) {

        val body = JsonObject().apply {
            addProperty("event_code", eventDetail.value?.code ?: "")
            addProperty("reg_id", registerId ?: "")
            add("ticket_options", Gson().toJsonTree(getCurrentTicketOption()))
        }

        val result = repo.updateRegisterInfo(body)

        if (result.isSuccessful().not()) {
            onHandleError(result.code, result.message)
        }

        return@withContext result.isSuccessful()
    }


    suspend fun registerEventWithKoa(event: EventItem, eBib: String): Boolean = withContext(IO) {

//        val ticketObjects = JsonArray()
//        event.ticket?.forEach { ticket ->
//            val ticketObject = JsonObject().apply {
//                addProperty("ticket_id", ticket.id)
//                addProperty("ticket_name", ticket.name)
//                addProperty("distance", ticket.distance ?: 0f)
//                addProperty("total_price", ticket.price ?: 0)
//            }
//            ticketObjects.add(ticketObject)
//        }
//        val ticketOptionObject = JsonObject().apply {
//            add("tickets", ticketObjects)
//            addProperty("total_price", 0)
//        }
//
//        val registerObject = JsonObject().apply {
//            add("ticket_options", ticketOptionObject)
//            addProperty("status", PaymentStatus.SUCCESS)
//            addProperty("payment_type", PaymentType.FREE)
//            addProperty("total_price", event.ticket?.get(0)?.price ?: 0)
//            addProperty("promo_code", "")
//            addProperty("discount_price", 0)
//            add("coupon", null)
//            addProperty("reg_date", System.currentTimeMillis().dateTimeFormat(SERVER_DATE_TIME_FORMAT))
//            addProperty("payment_date", System.currentTimeMillis().dateTimeFormat(SERVER_DATE_TIME_FORMAT))
//            addProperty("image", "")
//            add("partner", Gson().toJsonTree(event.partner))
//        }
//
//        val kaoObject = JsonObject().apply {
//            addProperty("slug", event.partner?.slug ?: "")
//            addProperty("ebib", eBib)
//        }
//
//        val body = JsonObject().apply {
//            addProperty("event_id", event.id)
//            add("regs", registerObject)
//            add("kao_request", kaoObject)
//        }
//
//
//        val result = repo.registerEventWithKao(body)
//        if (result.isSuccessful().not()) {
//            onHandleError(result.code, result.message)
//        }
//
//        return@withContext result.isSuccessful()

        //TODO("Disable for now")
        return@withContext false
    }


    override fun onCleared() {
        //allSubDistrict = null
        updateScreen.postValue(null)
        addressAutoFill.postValue(null)
        super.onCleared()
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val service = ApiService()
            return RegistrationViewModel(
                EventRepository(service.provideService(context, EventApi::class.java)),
                AddressRepository(service.provideService(context, AddressApi::class.java))
            ) as T
        }
    }
}


