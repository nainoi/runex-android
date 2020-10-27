package com.think.runex.feature.auth

import android.content.Context
import android.util.Log
import com.jozzee.android.core.datetime.dateTimeFormat
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.think.runex.datasource.BaseViewModel
import com.think.runex.datasource.Result
import com.think.runex.feature.auth.request.LoginCodeRequest
import com.think.runex.feature.user.UserInfo
import com.think.runex.java.App.App
import com.think.runex.java.App.AppEntity
import com.think.runex.java.Models.TokenObject
import com.think.runex.java.Models.UserObject
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class AuthViewModel(private val repo: AuthRepository) : BaseViewModel() {

    fun initialToken() {
        Logger.warning(simpleName(), "Initial token")
        val accessToken = repo.getLocalAccessToken()
        TokenManager.updateToken(accessToken)
        if (TokenManager.isAlive()) {
            Logger.info(simpleName(), "Token is alive")
        } else {
            Logger.error(simpleName(), "Token expired or not found.")
            TokenManager.clearToken()
        }
    }

    suspend fun loginWithCode(context: Context, code: String): Result<AccessToken>? = withContext(IO) {
        val loginResult = repo.loginWithCode(LoginCodeRequest(code))
        if (loginResult.isSuccessful().not()) {
            onHandleError(loginResult.statusCode, loginResult.message)
            TokenManager.clearToken()
            return@withContext null
        }
        loginResult.data?.also {
            updateAccessToken(it)
        }

        //Get User info
        val userResult = repo.userInfo()
        if (loginResult.isSuccessful().not()) {
            onHandleError(loginResult.statusCode, loginResult.message)
        }

        setTokenAndUserForJavaCode(context, loginResult.data, userResult.data)
        return@withContext loginResult
    }

    private fun updateAccessToken(accessToken: AccessToken) {
        TokenManager.updateToken(accessToken)
        repo.setLocalAccessToken(accessToken)
    }

    private fun setTokenAndUserForJavaCode(context: Context, accessToken: AccessToken?, userInfo: UserInfo?) {

        //Set access token
        val appEntity: AppEntity = App.instance(context).appEntity
        appEntity.setToken(TokenObject().apply {
            expire = (System.currentTimeMillis() + ((accessToken?.expiresIn ?: 0) * 1000))
                    .dateTimeFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            token = accessToken?.accessToken ?: ""
            code = 200
        })
        appEntity.setLoggedIn(true)
        App.instance(context).save(appEntity)

        //Set User Info
        val userObject = UserObject()
        userObject.data.apply {
            email = userInfo?.email
            fullname = userInfo?.fullName
            firstname = userInfo?.firstName
            lastname = userInfo?.lastName
            firstname_th = userInfo?.firstName
            lastname_th = userInfo?.lastName
            phone = userInfo?.phone
            avatar = userInfo?.avatar
            role = userInfo?.role
            birthdate = userInfo?.birthDate
            gender = userInfo?.gender
            created_at = userInfo?.createdAt
            updated_at = userInfo?.updatedAt
            isConfirm = userInfo?.isConfirmed ?: false
            address = ArrayList<UserObject.DataBean.AddressBean>().apply {
                userInfo?.address?.forEach { address ->
                    add(UserObject.DataBean.AddressBean().apply {
                        id = address.id
                        this.address = address.address
                        district = address.district
                        city = address.city
                        zipcode = address.zipCode
                        created_at = address.createdAt
                        updated_at = address.updatedAt
                    })
                }
            }
        }

        appEntity.setUser(userObject)
        App.instance(context).save(appEntity)
    }
}