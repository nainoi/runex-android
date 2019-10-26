package com.think.runex.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AuthViewModelFactory(private val authRepo: AuthRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = AuthViewModel(authRepo) as T
}