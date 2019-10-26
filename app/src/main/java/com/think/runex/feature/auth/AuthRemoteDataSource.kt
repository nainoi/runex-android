package com.think.runex.feature.auth

import com.jozzee.android.core.connection.NetworkMonitor
import com.think.runex.config.ERR_ON_INTERNET_CONNECTION
import com.think.runex.config.ERR_SOCKET_EXCEPTION
import com.think.runex.config.ERR_UNKNOWN
import com.think.runex.datasource.Result
import com.think.runex.datasource.remote.RemoteDataSource
import com.think.runex.feature.user.Profile
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import javax.net.ssl.HttpsURLConnection

class AuthRemoteDataSource(private val service: AuthApiService) : RemoteDataSource() {

    suspend fun register(body: RegisterRequest): Result<AccessToken> {
        if (NetworkMonitor.isConnected.not()) {
            return Result.error(ERR_ON_INTERNET_CONNECTION, "No Internet Connection")
        }
        return try {
            val result: AuthResponse = service.registerAsync(body).await()
            when (result.expire != "" && result.token != "") {
                true -> Result.success(AccessToken(token = result.token, expire = result.expire))
                false -> Result.error(result.statusCode, result.message)
            }
        } catch (e: HttpException) {
            e.printStackTrace()
            Result.error(e.code(), getHttpExceptionMessage(e))
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            Result.error(HttpsURLConnection.HTTP_CLIENT_TIMEOUT, e.message)
        } catch (e: SocketException) {
            e.printStackTrace()
            Result.error(ERR_SOCKET_EXCEPTION, e.message)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            Result.error(ERR_UNKNOWN, throwable.message)
        }
    }

    suspend fun login(body: LoginRequest): Result<AccessToken> {
        if (NetworkMonitor.isConnected.not()) {
            return Result.error(ERR_ON_INTERNET_CONNECTION, "No Internet Connection")
        }
        return try {
            val result: AuthResponse = service.loginAsync(body).await()
            when (result.expire != "" && result.token != "") {
                true -> Result.success(AccessToken(token = result.token, expire = result.expire))
                false -> Result.error(result.statusCode, result.message)
            }
        } catch (e: HttpException) {
            e.printStackTrace()
            Result.error(e.code(), getHttpExceptionMessage(e))
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            Result.error(HttpsURLConnection.HTTP_CLIENT_TIMEOUT, e.message)
        } catch (e: SocketException) {
            e.printStackTrace()
            Result.error(ERR_SOCKET_EXCEPTION, e.message)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            Result.error(ERR_UNKNOWN, throwable.message)
        }
    }

    suspend fun getProfile(): Result<Profile> = request(service.getProfileAsync())

}