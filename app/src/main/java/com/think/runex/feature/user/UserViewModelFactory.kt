package com.think.runex.feature.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.think.runex.datasource.remote.ApiService

class UserViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            UserViewModel(UserRepository(UserRemoteDataSource(ApiService().provideService(UserApiService::class.java)))) as T
}
