package com.think.runex.feature.event.all

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.think.runex.base.BaseViewModel
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.event.EventApi
import com.think.runex.feature.event.EventRepository
import com.think.runex.feature.event.data.EventItem
import com.think.runex.util.launchIoThread

class AllEventListViewModel(private val repo: EventRepository) : BaseViewModel() {

    //val pageSize: Int = 20

    val eventList: MutableLiveData<List<EventItem>> by lazy {
        MutableLiveData<List<EventItem>>()
    }

    var isLoading: Boolean = false
        private set

    //var isAllLoaded: Boolean = false
    //    private set

    fun getEventList(/*lastPosition: Int = 0*/) = launchIoThread {
        isLoading = true
        val result = repo.getAllEvents()
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }
        isLoading = false
        eventList.postValue(result.data)
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AllEventListViewModel(EventRepository(ApiService().provideService(context, EventApi::class.java))) as T
        }
    }
}