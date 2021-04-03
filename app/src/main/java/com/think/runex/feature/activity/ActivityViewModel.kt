package com.think.runex.feature.activity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.think.runex.base.BaseViewModel
import com.think.runex.datasource.api.ApiService

class ActivityViewModel(private val repo: ActivityRepository) : BaseViewModel() {

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ActivityViewModel(ActivityRepository(ApiService().provideService(context, ActivityApi::class.java))) as T
        }
    }
}