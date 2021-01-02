package com.think.runex.feature.event

import androidx.lifecycle.MutableLiveData
import com.think.runex.datasource.BaseViewModel
import com.think.runex.feature.event.model.Event
import com.think.runex.util.launchIoThread

class AllEventListViewModel(private val repo: EventRepository) : BaseViewModel() {

    //val pageSize: Int = 20

    val eventList: MutableLiveData<List<Event>> by lazy {
        MutableLiveData<List<Event>>()
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
}