package com.think.runex.feature.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.think.runex.datasource.api.ApiService
import com.think.runex.util.AppPreference

class AuthViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(AuthRepository(
                ApiService().provideService(AuthApi::class.java),
                AppPreference.createPreference(context))) as T
    }
}