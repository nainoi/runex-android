package com.think.runex.feature.event.detail

import androidx.lifecycle.MutableLiveData
import com.google.gson.*
import com.think.runex.base.BaseViewModel
import com.think.runex.feature.event.EventRepository
import com.think.runex.feature.event.data.EventDetail
import com.think.runex.feature.event.data.EventItem
import com.think.runex.util.launchIoThread
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

open class EventDetailsViewModel(val repo: EventRepository) : BaseViewModel() {

    val eventDetail: MutableLiveData<EventDetail> by lazy { MutableLiveData() }

    private var isRegisteredEvent: Boolean? = null

    fun getEventDetail(code: String) = launchIoThread {

        //Get event details.
        val result = repo.getEventDetails(code)
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }

        //Check registered event
        val isRegisteredResult = repo.isRegisteredEvent(code)
        isRegisteredEvent = isRegisteredResult.data?.isRegistered

        if (result.isSuccessful()) {
            eventDetail.postValue(result.data)
        }
    }

    suspend fun isRegisteredEvent(code: String): Boolean = withContext(IO) {
        if (isRegisteredEvent == null) {
            val result = repo.isRegisteredEvent(code)
            if (result.isSuccessful().not()) {
                onHandleError(result.statusCode, result.message)
            }
            isRegisteredEvent = result.data?.isRegistered
        }
        return@withContext isRegisteredEvent ?: false
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
//            onHandleError(result.statusCode, result.message)
//        }
//
//        return@withContext result.isSuccessful()

        //TODO("Disable for now")
        return@withContext false
    }

    override fun onCleared() {
        super.onCleared()
        eventDetail.postValue(null)
    }
}