package com.think.runex.feature.event.registered

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.think.runex.base.BaseViewModel
import com.think.runex.feature.event.EventRepository
import com.think.runex.feature.event.data.EventRegistered
import com.think.runex.feature.payment.data.PaymentStatus
import com.think.runex.util.launchIoThread
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class MyEventListViewModel(private val repo: EventRepository) : BaseViewModel() {

    val pageSize: Int = 20

    private var _myEvents: ArrayList<EventRegistered>? = null

    val myEvents: MutableLiveData<ArrayList<EventRegistered>> by lazy { MutableLiveData<ArrayList<EventRegistered>>() }

    var isLoading: Boolean = false
        private set

    var isAllLoaded: Boolean = false
        private set

    var filterByPaymentSuccess: Boolean = false
        set(value) {
            field = value
            myEvents.postValue(ArrayList(when (field) {
                true -> _myEvents?.filter { it.getPaymentStatus() == PaymentStatus.SUCCESS }
                false -> _myEvents
            } ?: emptyList()))
        }

    private var realStartPosition: Int? = null

    fun getEventList(startPosition: Int? = null) = launchIoThread {

        isLoading = true

        realStartPosition = when (filterByPaymentSuccess && startPosition != null) {
            true -> _myEvents?.size ?: 0
            false -> startPosition
        }

        val result = repo.getMyEvents()

        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }

        if (realStartPosition == null) {
            //Load single
            isAllLoaded = true
            _myEvents = ArrayList(result.data)
        } else {
            //Load more from position
            if (_myEvents == null) {
                _myEvents = ArrayList()
            }
            _myEvents?.addAll(result.data ?: emptyList())
        }

        myEvents.postValue(ArrayList(when (filterByPaymentSuccess) {
            true -> _myEvents?.filter { it.getPaymentStatus() == PaymentStatus.SUCCESS }
            false -> _myEvents
        } ?: emptyList()))

        //If result size less than page size it mean load all data.
        if (result.data?.size ?: 0 < pageSize) {
            isAllLoaded = true
        }

        isLoading = false
    }

    suspend fun getMyEventsForSubmitWorkout(): List<EventRegistered>? = withContext(IO) {
        val result = repo.getMyEventsAtActive()
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }

        //TODO("TODO Filter payment status success only for now")
        result.data = result.data?.filter {
            it.isPaymentSuccess() && it.eventDetail?.isOpenSendActivity == true
        }

        return@withContext result.data
    }
}