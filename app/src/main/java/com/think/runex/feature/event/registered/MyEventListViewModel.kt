package com.think.runex.feature.event.registered

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.think.runex.base.BaseViewModel
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.event.EventApi
import com.think.runex.feature.event.EventRepository
import com.think.runex.feature.event.data.Registered
import com.think.runex.feature.event.data.RegisterStatus
import com.think.runex.util.extension.launch
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class MyEventListViewModel(private val repo: EventRepository) : BaseViewModel() {

    val pageSize: Int = 20

    private var _myEvents: ArrayList<Registered>? = null

    val myEvents: MutableLiveData<ArrayList<Registered>> by lazy { MutableLiveData<ArrayList<Registered>>() }

    var isLoading: Boolean = false
        private set

    var isAllLoaded: Boolean = false
        private set

    var filterByRegisterSuccess: Boolean = false
        set(value) {
            field = value
            myEvents.postValue(ArrayList(when (field) {
                true -> _myEvents?.filter { it.getRegisterStatus(0) == RegisterStatus.SUCCESS }
                false -> _myEvents
            } ?: emptyList()))
        }

    private var realStartPosition: Int? = null

    fun getEventList(startPosition: Int? = null) = launch(IO) {

        isLoading = true

        realStartPosition = when (filterByRegisterSuccess && startPosition != null) {
            true -> _myEvents?.size ?: 0
            false -> startPosition
        }

        val result = repo.getMyEvents()

        if (result.isSuccessful().not()) {
            onHandleError(result.code, result.message)
        }

        if (realStartPosition == null) {
            //Load single
            isAllLoaded = true
            _myEvents = ArrayList(result.data ?: emptyList())
        } else {
            //Load more from position
            if (_myEvents == null) {
                _myEvents = ArrayList()
            }
            _myEvents?.addAll(result.data ?: emptyList())
        }

        myEvents.postValue(ArrayList(when (filterByRegisterSuccess) {
            true -> _myEvents?.filter { it.isRegisterSuccess(0) }
            false -> _myEvents
        } ?: emptyList()))

        //If result size less than page size it mean load all data.
        if (result.data?.size ?: 0 < pageSize) {
            isAllLoaded = true
        }

        isLoading = false
    }

    suspend fun getMyEventsForSubmitWorkout(): List<Registered>? = withContext(IO) {
        val result = repo.getMyEventsAtActive()
        if (result.isSuccessful().not()) {
            onHandleError(result.code, result.message)
        }

        //TODO("TODO Filter payment status success only for now")
        result.data = result.data?.filter {
            it.isRegisterSuccess(0) && it.eventDetail?.isOpenSendActivity == true
        }

        return@withContext result.data
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MyEventListViewModel(EventRepository(ApiService().provideService(context, EventApi::class.java))) as T
        }
    }
}