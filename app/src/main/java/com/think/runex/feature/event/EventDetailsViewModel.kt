package com.think.runex.feature.event

import com.google.gson.*
import com.think.runex.datasource.BaseViewModel
import com.think.runex.feature.event.model.EventDetail
import com.think.runex.feature.event.model.EventItem
import com.think.runex.feature.event.model.TicketEventDetail
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class EventDetailsViewModel(private val repo: EventRepository) : BaseViewModel() {

    var eventDetail: EventDetail? = null
        private set

    private var isRegisteredEvent: Boolean? = null

    suspend fun getEventDetail(code: String): Boolean = withContext(IO) {

        //Get event details.
        val result = repo.getEventDetails(code)
        when (result.isSuccessful()) {
            true -> eventDetail = result.data
            false -> onHandleError(result.statusCode, result.message)
        }

        //Check registered event
        isRegisteredEvent = isRegisteredEvent(code)

        return@withContext result.isSuccessful()
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
}