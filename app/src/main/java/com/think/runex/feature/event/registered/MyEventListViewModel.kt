package com.think.runex.feature.event.registered

import androidx.lifecycle.MutableLiveData
import com.think.runex.datasource.BaseViewModel
import com.think.runex.feature.event.EventRepository
import com.think.runex.feature.event.data.EventRegistered
import com.think.runex.feature.event.data.EventRegisteredData
import com.think.runex.util.launchIoThread

class MyEventListViewModel(private val repo: EventRepository) : BaseViewModel() {

    //val pageSize: Int = 20

    val myEvents: MutableLiveData<List<EventRegistered>> by lazy { MutableLiveData<List<EventRegistered>>() }

    var isLoading: Boolean = false
        private set

    //var isAllLoaded: Boolean = false
    //    private set

    fun getEventList(/*lastPosition: Int = 0*/) = launchIoThread {
        isLoading = true
        val result = repo.getMyEvents()
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }
        isLoading = false
        myEvents.postValue(result.data)
    }
}