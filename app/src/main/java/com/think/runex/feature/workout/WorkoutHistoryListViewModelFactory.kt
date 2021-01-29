package com.think.runex.feature.workout

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.think.runex.datasource.api.ApiService

class WorkoutHistoryListViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WorkoutHistoryListViewModel(WorkoutRepository(ApiService().provideService(context, WorkoutApi::class.java))) as T
    }
}