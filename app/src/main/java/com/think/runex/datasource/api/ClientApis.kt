package com.think.runex.datasource.api

import android.content.Context
import com.think.runex.feature.auth.AuthApi
import com.think.runex.feature.event.EventApi
import com.think.runex.feature.user.UserApi

object ClientApis {

    lateinit var authApi: AuthApi
        private set

    lateinit var userApi: UserApi
        private set

    lateinit var eventApi: EventApi
        private set

    fun initial(context: Context) {
        val service = ApiService()
        authApi = service.provideService(context, AuthApi::class.java)
        userApi = service.provideService(context, UserApi::class.java)
        eventApi = service.provideService(context, EventApi::class.java)
    }
}