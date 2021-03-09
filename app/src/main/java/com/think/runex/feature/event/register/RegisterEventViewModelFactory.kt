package com.think.runex.feature.event.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.address.AddressApi
import com.think.runex.feature.address.AddressRepository
import com.think.runex.feature.event.EventApi
import com.think.runex.feature.event.EventRepository
import com.think.runex.feature.payment.PaymentApi
import com.think.runex.feature.payment.PaymentRepository

class RegisterEventViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val service = ApiService()
        return RegisterEventViewModel(
                EventRepository(service.provideService(context, EventApi::class.java)),
                AddressRepository(service.provideService(context, AddressApi::class.java))) as T
    }
}