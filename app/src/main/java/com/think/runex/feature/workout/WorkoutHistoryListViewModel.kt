package com.think.runex.feature.workout

import androidx.lifecycle.MutableLiveData
import com.think.runex.datasource.BaseViewModel
import com.think.runex.feature.workout.model.WorkoutHistoryMonth
import com.think.runex.util.launchIoThread

class WorkoutHistoryListViewModel(private val repo: WorkoutRepository) : BaseViewModel() {

    //val pageSize: Int = 40

    val historyList: MutableLiveData<List<WorkoutHistoryMonth>> by lazy {
        MutableLiveData<List<WorkoutHistoryMonth>>()
    }

    var isLoading: Boolean = false
        private set

    //var isAllLoaded: Boolean = false
    //    private set

    fun getHistoryList(/*lastPosition: Int = 0*/) = launchIoThread {
        isLoading = true
        val result = repo.getWorkoutHistory()
        if (result.isSuccessful().not()) {
            onHandleError(result.statusCode, result.message)
        }
        isLoading = false
        historyList.postValue(result.data)
    }
}