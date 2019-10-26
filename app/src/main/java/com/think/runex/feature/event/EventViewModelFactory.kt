package com.think.runex.feature.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.think.runex.datasource.remote.ApiService

class EventViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            EventViewModel(EventRepository(ApiService().provideService(EventApiService::class.java))) as T
}