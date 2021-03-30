package com.think.runex.feature.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.think.runex.base.BaseViewModel
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.auth.data.AccessToken
import com.think.runex.feature.auth.data.TokenManager
import com.think.runex.feature.auth.data.request.AuthWithCodeBody
import com.think.runex.util.AppPreference
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class AuthViewModel(private val repo: AuthRepository) : BaseViewModel() {

    suspend fun updateAppConfig() = withContext(IO) {
        val result = repo.getAppConfig()

        /** If result have api config data will be save to shared preference
         *  but if get api failed will get api config from last config in shared preference instead.
         **/
        when (result.data != null) {
            true -> repo.setLocalAppConfig(result.data!!)
            false -> result.data = repo.getLocalAppConfig()
        }
        ApiConfig.updateAppConfig(result.data)
        return@withContext result.isSuccessful()
    }

    suspend fun initialToken() = withContext(IO) {
        Logger.warning(simpleName(), "initialToken")
        val accessToken = repo.getLocalAccessToken()
        TokenManager.updateToken(accessToken)
        Logger.warning(simpleName(), "Initial token completed, Token is alive: ${TokenManager.isAlive()}")

        //Check updated firebase token to server
        if (TokenManager.isAlive() && repo.isUpdatedFirebaseToken().not()) {
            repo.getFirebaseToken()?.also { firebaseToken ->
                Logger.warning(simpleName(), "Update firebase token to server")
                val result = repo.sendFirebaseTokenToServer(firebaseToken)
                if (result.isSuccessful()) {
                    repo.setUpdatedFirebaseToken(true)
                }
            }
        }
    }

    suspend fun loginWithCode(code: String): Boolean = withContext(IO) {
        val loginResult = repo.loginWithCode(AuthWithCodeBody(code))
        if (loginResult.isSuccessful().not()) {
            onHandleError(loginResult.statusCode, loginResult.message)
            TokenManager.clearToken()
            return@withContext false
        }
        loginResult.data?.also {
            updateAccessToken(it)
        }

        //Check updated firebase token to server
        if (TokenManager.isAlive() && repo.isUpdatedFirebaseToken().not()) {
            repo.getFirebaseToken()?.also { firebaseToken ->
                Logger.warning(simpleName(), "Update firebase token to server")
                val sendFirebaseResult = repo.sendFirebaseTokenToServer(firebaseToken)
                if (sendFirebaseResult.isSuccessful()) {
                    repo.setUpdatedFirebaseToken(true)
                }
            }
        }

        //Get User info for old java code
        val userResult = repo.getUserInfo()
        if (loginResult.isSuccessful().not()) {
            onHandleError(loginResult.statusCode, loginResult.message)
        }

        return@withContext loginResult.isSuccessful()
    }

    suspend fun logout(): Boolean = withContext(IO) {
        val result = repo.logout()
        when (result.isSuccessful()) {
            true -> {
                //Clear access token
                TokenManager.clearToken()
                repo.removeLocalAccessToken()
            }
            false -> onHandleError(result.statusCode, result.message)
        }
        return@withContext result.isSuccessful()
    }

    private fun updateAccessToken(accessToken: AccessToken) {
        TokenManager.updateToken(accessToken)
        repo.setLocalAccessToken(accessToken)
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(AuthRepository(
                    ApiService().provideService(context, AuthApi::class.java),
                    AppPreference.createPreference(context))) as T
        }
    }
}