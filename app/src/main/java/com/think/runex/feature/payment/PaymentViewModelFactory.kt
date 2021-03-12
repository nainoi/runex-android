package com.think.runex.feature.payment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.think.runex.datasource.api.ApiService

class PaymentViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PaymentViewModel(PaymentRepository(ApiService().provideService(context, PaymentApi::class.java))) as T
    }
}