package com.think.runex.feature.event.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.address.AddressApi
import com.think.runex.feature.address.AddressRepository
import com.think.runex.feature.event.EventApi
import com.think.runex.feature.event.EventRepository

class RegisterEventViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterEventViewModel(
                AddressRepository(ApiService().provideService(context, AddressApi::class.java)),
                EventRepository(ApiService().provideService(context, EventApi::class.java))) as T
    }
}