package com.think.runex.feature.auth

import android.content.Context
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.think.runex.datasource.BaseViewModel
import com.think.runex.datasource.Result
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.request.AuthenWithCodeRequest
import com.think.runex.feature.user.UserInfo
import com.think.runex.java.App.App
import com.think.runex.java.App.AppEntity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class AuthViewModel(private val repo: AuthRepository) : BaseViewModel() {

    suspend fun updateApiConfig() = withContext(IO) {
        val result = repo.getApiConfig()

        /** If result have api config data will be save to shared preference
         *  but if get api failed will get api config from last config in shared preference instead.
         **/
        when (result.data != null) {
            true -> repo.setLocalApiConfig(result.data!!)
            false -> result.data = repo.getLocalApiConfig()
        }
        ApiConfig.updateApiConfig(result.data)
        return@withContext result.isSuccessful()
    }

    fun initialToken() {
        Logger.warning(simpleName(), "initialToken")
        val accessToken = repo.getLocalAccessToken()
        TokenManager.updateToken(accessToken)
        Logger.warning(simpleName(), "Initial token completed, Token is alive: ${TokenManager.isAlive()}")
    }

    suspend fun loginWithCode(context: Context, code: String): Result<AccessToken>? = withContext(IO) {
        val loginResult = repo.loginWithCode(AuthenWithCodeRequest(code))
        if (loginResult.isSuccessful().not()) {
            onHandleError(loginResult.statusCode, loginResult.message)
            TokenManager.clearToken()
            return@withContext null
        }
        loginResult.data?.also {
            updateAccessToken(it)
        }

        //Get User info
        val userResult = repo.getUserInfo()
        if (loginResult.isSuccessful().not()) {
            onHandleError(loginResult.statusCode, loginResult.message)
        }

        setUserForJavaCode(context, userResult.data)
        return@withContext loginResult
    }

    private fun updateAccessToken(accessToken: AccessToken) {
        TokenManager.updateToken(accessToken)
        repo.setLocalAccessToken(accessToken)
    }

    private fun setUserForJavaCode(context: Context, userInfo: UserInfo?) {
        //Set access token
        val appEntity: AppEntity = App.instance(context).appEntity
        appEntity.setUser(userInfo)
        App.instance(context).save(appEntity)
    }
}