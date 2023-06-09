package com.think.runex.datasource.api

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.edit
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.jozzee.android.core.text.isJsonFormat
import com.jozzee.android.core.util.Logger
import com.think.runex.util.extension.toJson
import com.think.runex.config.AUTHORIZATION
import com.think.runex.feature.auth.data.request.RefreshTokenBody
import com.think.runex.util.AppPreference
import com.think.runex.config.KEY_ACCESS_TOKEN
import com.think.runex.config.KEY_DATA
import com.think.runex.config.KEY_REFRESH_TOKEN
import com.think.runex.feature.auth.data.AccessToken
import com.think.runex.feature.auth.data.TokenManager
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.net.UnknownHostException

class TokenInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()

        if (ApiConfig.BASE_URL.contains(request.url.host) && TokenManager.isAlive()) {
            //Add access token to header of request
            request = request.newBuilder()
                .addHeader(AUTHORIZATION, TokenManager.accessToken())
                .build()
        }

        var response = chain.proceed(request)
        var refreshTokenResponse: Response? = null

        try {
            //Get body as string
//            val body: String? = response.body?.source()?.let { source ->
//                source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
//                return@let response.body?.contentType()?.charset(StandardCharsets.UTF_8)?.let { charset ->
//                    source.buffer.clone().readString(charset)
//                }
//            }

            if (response.code == 401 || response.code == 444) {
                response.close()

                //Log.w("RefreshTokenInterceptor", "Code: ${response.code} Auto refresh token!!")
                val body = RefreshTokenBody(TokenManager.accessToken(), TokenManager.refreshToken)
                val refreshTokenRequest = Request.Builder()
                    .url(ApiConfig.REFRESH_TOKEN_URL)
                    .post(body.toJson().toRequestBody("application/json; charset=UTF-8".toMediaType()))
                    .build()

                refreshTokenResponse?.close()
                refreshTokenResponse = chain.proceed(refreshTokenRequest)

                if (refreshTokenResponse.code == 200) {
                    refreshTokenResponse.body?.string()?.also { bodyString ->
                        //Log.w("RefreshTokenInterceptor", "Refresh token response: $bodyString")
                        if (bodyString.isJsonFormat()) {
                            val jsonObject = Gson().fromJson(bodyString, JsonElement::class.java).asJsonObject
                            if (jsonObject.has(KEY_DATA)) {
                                val tokenObject = jsonObject.get(KEY_DATA).asJsonObject
                                val accessTokenText = tokenObject.get(KEY_ACCESS_TOKEN).asString
                                    ?: ""
                                val refreshTokenText = tokenObject.get(KEY_REFRESH_TOKEN).asString
                                    ?: ""
                                if (accessTokenText.isNotBlank() && refreshTokenText.isNotBlank()) {
                                    //Log.e("RefreshTokenInterceptor", "Access token: $accessTokenText")
                                    //Log.e("RefreshTokenInterceptor", "Refresh token: $refreshTokenText")
                                    val accessToken = AccessToken(accessTokenText, refreshTokenText)
                                    //Update token for kotlin
                                    TokenManager.updateToken(accessToken)
                                    AppPreference.createPreference(context).edit {
                                        putString(KEY_ACCESS_TOKEN, accessToken.toJson())
                                    }
                                    refreshTokenResponse.close()

                                    //re new request
                                    response = chain.proceed(
                                        request.newBuilder()
                                            .removeHeader(AUTHORIZATION)
                                            .addHeader(AUTHORIZATION, TokenManager.accessToken())
                                            .build()
                                    )

                                }
                            }
                        }
                    }
                    refreshTokenResponse.close()
                }
            }

//            if (body?.contains(ERR_MSG_UNAUTHORIZED, true) == true) {
//                return response.newBuilder()
//                        .code(HttpsURLConnection.HTTP_UNAUTHORIZED)
//                        .body(Result(HttpsURLConnection.HTTP_UNAUTHORIZED, body, null).toJson().toResponseBody())
//                        .build()
//
//            }

        } catch (error: UnknownHostException) {
            error.printStackTrace()
        } catch (error: IllegalStateException) {
            error.printStackTrace()
        } catch (error: Throwable) {
            error.printStackTrace()
        } finally {
            refreshTokenResponse?.close()
            return response
        }
    }
}