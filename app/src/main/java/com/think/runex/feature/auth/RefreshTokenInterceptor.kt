package com.think.runex.feature.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.jozzee.android.core.text.isJsonFormat
import com.think.runex.common.toJson
import com.think.runex.common.toObject
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.request.RefreshTokenRequest
import com.think.runex.java.App.App
import com.think.runex.java.App.App.APP_ENTITY
import com.think.runex.java.App.AppEntity
import com.think.runex.java.Constants.Globals
import com.think.runex.java.Models.TokenObject
import com.think.runex.util.AppPreference
import com.think.runex.util.KEY_ACCESS_TOKEN
import com.think.runex.util.KEY_DATA
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.UnknownHostException
import java.nio.charset.StandardCharsets
import javax.net.ssl.HttpsURLConnection

class RefreshTokenInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)
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
                Log.w("RefreshTokenInterceptor", "Code: ${response.code} Auto refresh token!!")
                val body = RefreshTokenRequest(TokenManager.accessToken, TokenManager.refreshToken)
                val refreshTokenRequest = Request.Builder()
                        .url(ApiConfig.REFRESH_TOKEN_URL)
                        .post(body.toJson().toRequestBody("application/json; charset=UTF-8".toMediaType()))
                        .build()
                val refreshTokenResponse = chain.proceed(refreshTokenRequest)
                if (refreshTokenResponse.code == 200) {
                    refreshTokenResponse.body?.string()?.also { bodyString ->
                        Log.w("RefreshTokenInterceptor", "Refresh token response: $bodyString")
                        if (bodyString.isJsonFormat()) {
                            val jsonObject = Gson().fromJson(bodyString, JsonElement::class.java).asJsonObject
                            if (jsonObject.has(KEY_DATA)) {
                                val tokenObject = jsonObject.get(KEY_DATA).asJsonObject
                                val accessTokenText = tokenObject.get("access_token").asString ?: ""
                                val refreshTokenText = tokenObject.get("refresh_token").asString
                                        ?: ""
                                if (accessTokenText.isNotBlank() && refreshTokenText.isNotBlank()) {
                                    Log.e("RefreshTokenInterceptor", "Access token: $accessTokenText")
                                    Log.e("RefreshTokenInterceptor", "Refresh token: $refreshTokenText")
                                    val accessToken = AccessToken(accessTokenText, refreshTokenText)
                                    //Update token for kotlin
                                    TokenManager.updateToken(accessToken)
                                    AppPreference.createPreference(context).edit {
                                        putString(KEY_ACCESS_TOKEN, accessToken.toJson())
                                    }
                                    refreshTokenResponse.close()

                                    //re new request
                                    response = chain.proceed(request.newBuilder()
                                            .removeHeader(Globals.HEADER_AUTHORIZATION)
                                            .addHeader(Globals.HEADER_AUTHORIZATION, TokenManager.accessToken)
                                            .build())

                                }
                            }
                        }
                    }
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
            return response
        }
    }
}