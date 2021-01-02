package com.think.runex.feature.event

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.think.runex.datasource.api.ApiService

class AllEventListViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllEventListViewModel(EventRepository(ApiService().provideService(context, EventApi::class.java))) as T
    }
}