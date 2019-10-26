package com.think.runex.utility

import android.content.Context
import com.think.runex.datasource.local.getAppPreference
import com.think.runex.datasource.remote.ApiService
import com.think.runex.feature.auth.*
import com.think.runex.feature.event.EventViewModelFactory
import com.think.runex.feature.user.UserViewModelFactory

/**
 * Static methods used to inject classes needed for various Activities and Fragments.
 */
object InjectorUtils {

    fun provideAuthViewModelFactory(context: Context): AuthViewModelFactory {
        return AuthViewModelFactory(AuthRepository(AuthRemoteDataSource(ApiService().provideService(AuthApiService::class.java)),
                AuthLocalDataSource(context.getAppPreference())))
    }

    fun provideUserViewModelFactory(): UserViewModelFactory {
        return UserViewModelFactory()
    }

    fun provideEventListViewModelFactory(): EventViewModelFactory {
        return EventViewModelFactory()
    }
}