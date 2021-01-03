package com.think.runex.feature.event

import com.google.gson.*
import com.jozzee.android.core.datetime.dateTimeFormat
import com.think.runex.datasource.BaseViewModel
import com.think.runex.feature.payment.PaymentStatus
import com.think.runex.feature.payment.PaymentType
import com.think.runex.config.SERVER_DATE_TIME_FORMAT
import com.think.runex.feature.event.model.Event
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class EventViewModel(private val repo: EventRepository) : BaseViewModel() {

    suspend fun isRegisteredEvent(eventId: String): Boolean = withContext(IO) {
        val result = repo.isRegisteredEvent(eventId)
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }
        return@withContext result.data?.isRegistered ?: false
    }

    suspend fun registerEventWithKoa(event: Event, eBib: String): Boolean = withContext(IO) {

        val ticketObjects = JsonArray()
        event.ticket?.forEach { ticket ->
            val ticketObject = JsonObject().apply {
                addProperty("ticket_id", ticket.id)
                addProperty("ticket_name", ticket.title)
                addProperty("distance", ticket.distance ?: 0f)
                addProperty("total_price", ticket.price ?: 0)
            }
            ticketObjects.add(ticketObject)
        }
        val ticketOptionObject = JsonObject().apply {
            add("tickets", ticketObjects)
            addProperty("total_price", 0)
        }

        val registerObject = JsonObject().apply {
            add("ticket_options", ticketOptionObject)
            addProperty("status", PaymentStatus.SUCCESS)
            addProperty("payment_type", PaymentType.FREE)
            addProperty("total_price", event.ticket?.get(0)?.price ?: 0)
            addProperty("promo_code", "")
            addProperty("discount_price", 0)
            add("coupon", null)
            addProperty("reg_date", System.currentTimeMillis().dateTimeFormat(SERVER_DATE_TIME_FORMAT))
            addProperty("payment_date", System.currentTimeMillis().dateTimeFormat(SERVER_DATE_TIME_FORMAT))
            addProperty("image", "")
            add("partner", Gson().toJsonTree(event.partner))
        }

        val kaoObject = JsonObject().apply {
            addProperty("slug", event.partner?.slug ?: "")
            addProperty("ebib", eBib)
        }

        val body = JsonObject().apply {
            addProperty("event_id", event.id)
            add("regs", registerObject)
            add("kao_request", kaoObject)
        }


        val result = repo.registerEventWithKao(body)
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }

        return@withContext result.isSuccessful()
    }
}