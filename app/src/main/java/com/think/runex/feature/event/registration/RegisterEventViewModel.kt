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
import com.think.runex.config.SERVER_DATE_TIME_FORMAT
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.address.AddressApi
import com.think.runex.feature.address.AddressRepository
import com.think.runex.feature.address.data.AddressAutoFill
import com.think.runex.feature.address.data.SubDistrict
import com.think.runex.feature.event.EventApi
import com.think.runex.feature.event.EventRepository
import com.think.runex.feature.event.data.*
import com.think.runex.feature.event.data.request.EventRegistrationBody
import com.think.runex.feature.event.data.request.TicketOptionEventRegistrationBody
import com.think.runex.feature.event.data.request.UserOptionEventRegistrationBody
import com.think.runex.feature.event.detail.EventDetailsViewModel
import com.think.runex.util.launchIoThread
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class RegisterEventViewModel(eventRepo: EventRepository,
                             private val addressRepo: AddressRepository) : EventDetailsViewModel(eventRepo) {

    private var allSubDistrictList: List<SubDistrict>? = null
    private var firstThreeLettersQuery: String = ""

    val updateScreen: MutableLiveData<String> by lazy { MutableLiveData() }

    val addressAutoFill: MutableLiveData<AddressAutoFill> by lazy { MutableLiveData() }

    var currentNo: Int = 1
        private set

    var ticketOptions: ArrayList<TicketOptionEventRegistrationBody> = ArrayList()
        private set

    var subDistricts: ArrayList<SubDistrict> = ArrayList()
        private set


    fun onGetEventDetailCompleted() {
        updateScreen.postValue(ChooseTicketFragment::class.java.simpleName)
    }

    fun getCurrentTicketOption(): TicketOptionEventRegistrationBody? {
        if (ticketOptions.isEmpty()) {
            ticketOptions.add(TicketOptionEventRegistrationBody())
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

    fun setUserData(userOption: UserOptionEventRegistrationBody) {
        getCurrentTicketOption()?.apply {
            this.userOption = userOption
            updateScreen.postValue(ConfirmRegistrationFragment::class.java.simpleName)
        }
    }


    fun searchAddressByZipCode(zipCode: String, viewRequestId: Int) = launchIoThread {
        Log.v("Jozzee", "searchAddressByZipCode: $zipCode")
        val result = addressRepo.getSubDistrictByZipCode(zipCode)
        when (result.isSuccessful()) {
            true -> {
                firstThreeLettersQuery = ""
                allSubDistrictList = result.data

                val autoFillList = allSubDistrictList?.map { it.getFullAddress() } ?: emptyList()
                addressAutoFill.postValue(AddressAutoFill(viewRequestId, autoFillList.toTypedArray()))
            }
            false -> onHandleError(result.statusCode, result.message, "Address")
        }
    }

    fun searchAddressBySubDistricts(query: String, viewRequestId: Int) = launchIoThread {
        Log.v("Jozzee", "searchAddressBySubDistricts: $query")
        if (addressAutoFill.value?.viewRequestId == viewRequestId && query.startsWith(firstThreeLettersQuery)) {
            val autoFillList = ArrayList<String>()
            allSubDistrictList?.forEach { address ->
                if (address.subDistrict?.toLowerCase(Locale.getDefault())?.contains(query.toLowerCase(Locale.getDefault())) == true) {
                    autoFillList.add(address.getFullAddress())
                }
            }
            addressAutoFill.postValue(AddressAutoFill(viewRequestId, autoFillList.toTypedArray()))
            return@launchIoThread
        }

        val result = addressRepo.getSubDistrictBySubDistrict(query)
        when (result.isSuccessful()) {
            true -> {
                firstThreeLettersQuery = query
                allSubDistrictList = result.data

                val autoFillList = allSubDistrictList?.map { it.getFullAddress() } ?: emptyList()
                addressAutoFill.postValue(AddressAutoFill(viewRequestId, autoFillList.toTypedArray()))
            }
            false -> onHandleError(result.statusCode, result.message, "Address")
        }
    }

    fun searchAddressByDistricts(query: String, viewRequestId: Int) = launchIoThread {
        Log.v("Jozzee", "searchAddressByDistricts: $query")
        if (addressAutoFill.value?.viewRequestId == viewRequestId && query.startsWith(firstThreeLettersQuery)) {
            val autoFillList = ArrayList<String>()
            allSubDistrictList?.forEach { address ->
                if (address.district?.toLowerCase(Locale.getDefault())?.contains(query.toLowerCase(Locale.getDefault())) == true) {
                    autoFillList.add(address.getFullAddress())
                }
            }
            addressAutoFill.postValue(AddressAutoFill(viewRequestId, autoFillList.toTypedArray()))
            return@launchIoThread
        }

        val result = addressRepo.getSubDistrictByDistrict(query)
        when (result.isSuccessful()) {
            true -> {
                firstThreeLettersQuery = query
                allSubDistrictList = result.data

                val autoFillList = allSubDistrictList?.map { it.getFullAddress() } ?: emptyList()
                addressAutoFill.postValue(AddressAutoFill(viewRequestId, autoFillList.toTypedArray()))
            }
            false -> onHandleError(result.statusCode, result.message, "Address")
        }
    }

    fun searchAddressByProvince(query: String, viewRequestId: Int) = launchIoThread {
        Log.v("Jozzee", "searchAddressByProvince: $query")
        if (addressAutoFill.value?.viewRequestId == viewRequestId && query.startsWith(firstThreeLettersQuery)) {
            val autoFillList = ArrayList<String>()
            allSubDistrictList?.forEach { address ->
                if (address.province?.toLowerCase(Locale.getDefault())?.contains(query.toLowerCase(Locale.getDefault())) == true) {
                    autoFillList.add(address.getFullAddress())
                }
            }
            addressAutoFill.postValue(AddressAutoFill(viewRequestId, autoFillList.toTypedArray()))
            return@launchIoThread
        }

        val result = addressRepo.getSubDistrictByProvince(query)
        when (result.isSuccessful()) {
            true -> {
                firstThreeLettersQuery = query
                allSubDistrictList = result.data

                val autoFillList = allSubDistrictList?.map { it.getFullAddress() } ?: emptyList()
                addressAutoFill.postValue(AddressAutoFill(viewRequestId, autoFillList.toTypedArray()))
            }
            false -> onHandleError(result.statusCode, result.message, "Address")
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

    suspend fun registerEvent(): EventRegistered? = withContext(IO) {

        ticketOptions.forEach { ticketOption ->
            ticketOption.receiptType = when (eventDetail.value?.category) {
                EventCategory.VIRTUAL_RUN -> "myself"
                else -> ""
            }
        }

        val registerBody = EventRegistrationBody().apply {
            this.event = eventDetail.value
            this.totalPrice = this@RegisterEventViewModel.ticketOptions.sumByDouble { it.totalPrice }
            this.registerDate = System.currentTimeMillis().dateTimeFormat(SERVER_DATE_TIME_FORMAT)
            this.ticketId = getCurrentTicketOption()?.ticket?.id ?: ""
            this.ticketOptions = this@RegisterEventViewModel.ticketOptions
        }
        val body = JsonObject().apply {
            addProperty("event_id", eventDetail.value?.id ?: 0)
            addProperty("event_code", eventDetail.value?.code ?: "")
            addProperty("owner_id", eventDetail.value?.ownerId ?: "")
            add("regs", Gson().toJsonTree(registerBody))
        }

        val result = repo.registerEvent(body)
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }
        return@withContext result.data
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
            return RegisterEventViewModel(
                    EventRepository(service.provideService(context, EventApi::class.java)),
                    AddressRepository(service.provideService(context, AddressApi::class.java))) as T
        }
    }
}


