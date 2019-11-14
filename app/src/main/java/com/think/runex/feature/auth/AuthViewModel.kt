package com.think.runex.feature.auth

import androidx.lifecycle.viewModelScope
import com.jozzee.android.core.simpleName
import com.jozzee.android.core.utility.Logger
import com.think.runex.datasource.BaseViewModel
import com.think.runex.datasource.Result
import com.think.runex.feature.user.Profile
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepo: AuthRepository) : BaseViewModel() {

    fun initialToken() {
        Logger.warning(simpleName(), "Initial token")
        val accessToken = authRepo.getAccessToken()
        TokenManager.updateToken(accessToken.token, accessToken.tokenType, accessToken.getExpiresIn())
        if (TokenManager.isAlive()) {
            Logger.info(simpleName(), "Token is alive")
        } else {
            Logger.error(simpleName(), "Token expired or not found.")
            TokenManager.clearToken()
        }
    }

    suspend fun register(body: RegisterRequest): Profile? {
        var profileResult = Result<Profile>()
        viewModelScope.launch(IO) {
            val registerResult = authRepo.register(body)
            if (registerResult.isSuccessful().not()) {
                onHandleError(registerResult.statusCode, registerResult.message)
                TokenManager.clearToken()
                return@launch
            }
            registerResult.data?.also { updateAccessToken(it) }
            profileResult = authRepo.getProfile()
            if (profileResult.isSuccessful().not()) {
                onHandleError(profileResult.statusCode, profileResult.message)
                return@launch
            }
        }.join()
        return profileResult.data
    }

    suspend fun registerWithSocialPlatfrom() {

    }

    suspend fun login(email: String, password: String): Profile? {
        var profileResult = Result<Profile>()
        viewModelScope.launch(IO) {
            val loginResult = authRepo.login(LoginRequest(email, password))
            if (loginResult.isSuccessful().not()) {
                onHandleError(loginResult.statusCode, loginResult.message)
                TokenManager.clearToken()
                return@launch
            }
            loginResult.data?.also { updateAccessToken(it) }
            profileResult = authRepo.getProfile()
            if (profileResult.isSuccessful().not()) {
                onHandleError(profileResult.statusCode, profileResult.message)
                return@launch
            }
        }.join()
        return profileResult.data
    }

    private fun updateAccessToken(accessToken: Token) {
        TokenManager.updateToken(accessToken.token, accessToken.tokenType, accessToken.getExpiresIn())
        authRepo.setAccessToken(accessToken)
    }

    private suspend fun getProfile(): Profile? {
        val result = authRepo.getProfile()
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }
        return result.data
    }
}