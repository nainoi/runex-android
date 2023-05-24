package com.think.runex.datasource.api

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.jozzee.android.core.connection.NetworkMonitor
import com.jozzee.android.core.text.isJsonFormat
import com.think.runex.config.*
import com.think.runex.datasource.Result
import com.think.runex.datasource.ResultAuth
import com.think.runex.util.*
import com.think.runex.util.extension.toJson
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.nio.charset.StandardCharsets
import javax.net.ssl.HttpsURLConnection

open class RemoteDataSource {

    suspend fun <T> call(job: Deferred<Result<T>>): Result<T> = withContext(Dispatchers.IO) {
        if (NetworkMonitor.isConnected.not()) {
            return@withContext Result.error<T>(ERR_NO_INTERNET_CONNECTION, "No Internet Connection")
        }

        return@withContext try {
            job.await().apply {
                code = HttpsURLConnection.HTTP_OK
            }
        } catch (e: HttpException) {
            e.printStackTrace()
            e.logToFirebase()
            Result.error(e.code(), getHttpExceptionMessage(e))
        } catch (e: SocketTimeoutException) {
            Result.error(HttpsURLConnection.HTTP_CLIENT_TIMEOUT, e.message)
        } catch (e: SocketException) {
            e.printStackTrace()
            Result.error(ERR_SOCKET_EXCEPTION, e.message)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            Result.error(ERROR_JSON_FORMAT, e.message)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            Result.error(ERR_NO_STATUS_CODE, throwable.message)
        }
    }

    suspend fun <T> calls(job: Deferred<T>): Result<T> = withContext(Dispatchers.IO) {
        if (NetworkMonitor.isConnected.not()) {
            return@withContext Result.error<T>(ERR_NO_INTERNET_CONNECTION, "No Internet Connection")
        }
        return@withContext try {
            Result.success(job.await(), null)
        } catch (e: HttpException) {
            e.printStackTrace()
            e.logToFirebase()
            Result.error(e.code(), getHttpExceptionMessage(e))
        } catch (e: SocketTimeoutException) {
            Result.error(HttpsURLConnection.HTTP_CLIENT_TIMEOUT, e.message)
        } catch (e: SocketException) {
            e.printStackTrace()
            Result.error(ERR_SOCKET_EXCEPTION, e.message)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            Result.error(ERR_NO_STATUS_CODE, throwable.message)
        }
    }

    suspend fun <T> callAuth(job: Deferred<T>): ResultAuth<T> = withContext(Dispatchers.IO) {
        if (NetworkMonitor.isConnected.not()) {
            return@withContext ResultAuth.error<T>(false, "No Internet Connection")
        }
        return@withContext try {
            ResultAuth.success(job.await(), true)
        } catch (e: HttpException) {
            e.printStackTrace()
            e.logToFirebase()
            ResultAuth.error(false, getHttpExceptionMessage(e))
        } catch (e: SocketTimeoutException) {
            ResultAuth.error(false, e.message)
        } catch (e: SocketException) {
            e.printStackTrace()
            ResultAuth.error(false, e.message)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            ResultAuth.error(false, throwable.message)
        }
    }

    fun getHttpExceptionMessage(exception: HttpException): String {

        val stringBody: String? = exception.getErrorBody()

        if (stringBody?.isJsonFormat() == true) {
            val jsonObject = Gson().fromJson(stringBody, JsonElement::class.java).asJsonObject
            if (jsonObject.has(KEY_MESSAGE)) {
                return jsonObject.get(KEY_MESSAGE).asString
            } else if (jsonObject.has(KEY_ERROR)) {
                return jsonObject.get(KEY_ERROR).asString
            }
        }
        return stringBody ?: exception.message()
    }

    private fun HttpException.logToFirebase() {
        try {
            val errorBody = JsonObject().apply {
                addProperty("Url", response()?.raw()?.request?.url?.toString() ?: "")
                addProperty("Method", response()?.raw()?.request?.method ?: "")
                addProperty("Request", response()?.raw()?.request?.body?.toString() ?: "")
                addProperty("Response", getErrorBody() ?: "")
            }
            FirebaseCrashlytics.getInstance().recordException(ApiException(errorBody.toJson()))

        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun HttpException.getErrorBody(): String? {
        return response()?.errorBody()?.source()?.use { source ->
            source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            return@use response()?.errorBody()?.contentType()?.charset(StandardCharsets.UTF_8)?.let { charset ->
                return@let source.buffer.clone().readString(charset)
            }
        }
    }
}