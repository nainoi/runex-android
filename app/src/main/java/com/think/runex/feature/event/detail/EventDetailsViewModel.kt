package com.think.runex.feature.event.detail

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.*
import com.think.runex.base.BaseViewModel
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.event.EventApi
import com.think.runex.feature.event.EventRepository
import com.think.runex.feature.event.data.EventDetail
import com.think.runex.feature.event.data.EventItem
import com.think.runex.util.extension.launch
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

open class EventDetailsViewModel(val repo: EventRepository) : BaseViewModel() {

    val eventDetail: MutableLiveData<EventDetail> by lazy { MutableLiveData() }

    private var isRegisteredEvent: Boolean? = null

    fun getEventDetail(eventCode: String) = launch(IO) {

        //Get event details.
        val result = repo.getEventDetails(eventCode)
        if (result.isSuccessful().not()) {
            onHandleError(result.code, result.message)
        }

        //Check registered event
        val isRegisteredResult = repo.isRegisteredEvent(eventCode)
        isRegisteredEvent = isRegisteredResult.data?.isRegistered

        if (result.isSuccessful()) {
            eventDetail.postValue(result.data)
        }
    }

    suspend fun isRegisteredEvent(eventCode: String): Boolean = withContext(IO) {
        if (isRegisteredEvent == null) {
            val result = repo.isRegisteredEvent(eventCode)
            if (result.isSuccessful().not()) {
                onHandleError(result.code, result.message)
            }
            isRegisteredEvent = result.data?.isRegistered
        }
        return@withContext isRegisteredEvent ?: false
    }

    override fun onCleared() {
        super.onCleared()
        eventDetail.postValue(null)
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EventDetailsViewModel(EventRepository(ApiService().provideService(context, EventApi::class.java))) as T
        }
    }
}