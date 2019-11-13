package com.think.runex.feature.event

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitObjectResult
import com.github.kittinunf.fuel.gson.gsonDeserializer
import com.jozzee.android.core.connection.NetworkMonitor
import com.jozzee.android.core.utility.Logger
import com.think.runex.common.getErrorMessage
import com.think.runex.config.ERR_NO_INTERNET_CONNECTION
import com.think.runex.datasource.Result

class EventRepository {

    suspend fun getAllEvents(status: String? = null): Result<List<Event>> {
        if (NetworkMonitor.isConnected.not()) {
            return Result.error(ERR_NO_INTERNET_CONNECTION, "No Internet Connection")
        }

        return when (status.isNullOrBlank()) {
            true -> getAllEvents()
            false -> getAllEventsByStatus(status)
        }
    }

    private suspend fun getAllEvents(): Result<List<Event>> {
        return Fuel.get(EventUrl.ALL_EVENT_PATH)
                //.authentication()
                //.bearer(TokenManager.bearerToken())
                .response { request, response, _ ->
                    if (Logger.isLogging) {
                        println(request)
                        println(response)
                    }
                }
                .awaitObjectResult(gsonDeserializer<Result<List<Event>>>())
                .fold(success = {
                    it
                }, failure = { error ->
                    Result.error(error.response.statusCode, error.getErrorMessage())
                })
    }

    private suspend fun getAllEventsByStatus(status: String? = null): Result<List<Event>> {
        return Fuel.get(EventUrl.ALL_EVENT_BY_STATUS_PATH, listOf("status" to status))
                //.authentication()
                //.bearer(TokenManager.bearerToken())
                .response { request, response, _ ->
                    if (Logger.isLogging) {
                        println(request)
                        println(response)
                    }
                }
                .awaitObjectResult(gsonDeserializer<Result<List<Event>>>())
                .fold(success = {
                    it
                }, failure = { error ->
                    Result.error(error.response.statusCode, error.getErrorMessage())
                })
    }
}