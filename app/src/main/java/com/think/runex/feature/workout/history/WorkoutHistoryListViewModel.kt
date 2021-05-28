package com.think.runex.feature.workout.history

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jozzee.android.core.datetime.toTimeMillis
import com.think.runex.base.BaseViewModel
import com.think.runex.config.SERVER_DATE_TIME_FORMAT
import com.think.runex.config.SERVER_DATE_TIME_FORMAT_2
import com.think.runex.feature.workout.WorkoutRepository
import com.think.runex.feature.workout.data.WorkoutHistoryMonth
import com.think.runex.feature.workout.data.WorkoutInfo
import com.think.runex.util.extension.launch
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class WorkoutHistoryListViewModel(private val repo: WorkoutRepository) : BaseViewModel() {

    //val pageSize: Int = 40

    val historyList: MutableLiveData<List<WorkoutHistoryMonth>> by lazy {
        MutableLiveData<List<WorkoutHistoryMonth>>()
    }

    var isLoading: Boolean = false
        private set

    //var isAllLoaded: Boolean = false
    //    private set

    fun getHistoryList(/*lastPosition: Int = 0*/) = launch(IO) {
        isLoading = true
        val result = repo.getWorkoutHistory()
        if (result.isSuccessful().not()) {
            onHandleError(result.code, result.message)
        }

        //Sort year and month
        result.data = ArrayList(result.data?.sortedWith(compareByDescending<WorkoutHistoryMonth> {
            it.year
        }.thenByDescending {
            it.month
        }) ?: emptyList())

        //Sort day in month
        result.data?.forEachIndexed { index, workoutHistoryMonth ->

            workoutHistoryMonth.workouts = workoutHistoryMonth.workouts?.sortedByDescending {
                dateServerToMillis(it.workoutDate)
            }?.toMutableList()

            //result.data?.set(index, workoutHistoryMonth)
        }
        isLoading = false
        historyList.postValue(result.data)
    }

    private fun dateServerToMillis(workoutDate: String?): Long {
        return try {
            workoutDate?.toTimeMillis(SERVER_DATE_TIME_FORMAT) ?: 0
        } catch (e: Throwable) {
            try {
                workoutDate?.toTimeMillis(SERVER_DATE_TIME_FORMAT_2) ?: 0
            } catch (e: Throwable) {
                e.printStackTrace()
                0
            }
        }
    }

    suspend fun deleteWorkout(
        monthPosition: Int,
        workoutInfo: WorkoutInfo
    ): Boolean = withContext(IO) {
        isLoading = true

        val body = JsonObject().apply {
            add("activity_info", Gson().toJsonTree(workoutInfo))
        }

        val result = repo.deleteWorkout(body)

        if (result.isSuccessful()) {

            val arrayList = ArrayList<WorkoutHistoryMonth>(historyList.value ?: emptyList())

            if (monthPosition < arrayList.size) {

                arrayList[monthPosition].also { montHistory ->

                    val infoList = ArrayList<WorkoutInfo>(montHistory.workouts ?: emptyList())
                    run loop@{
                        for (i in (infoList.size - 1) downTo 0) {
                            if (infoList[i].id == workoutInfo.id) {
                                infoList.removeAt(i)
                                return@loop
                            }
                        }
                    }

                    montHistory.workouts = infoList
                    arrayList[monthPosition] = montHistory

                    if (montHistory.workouts?.isNullOrEmpty() == true) {
                        arrayList.removeAt(monthPosition)
                    }
                }
            }

            historyList.postValue(arrayList)
        } else {
            onHandleError(result.code, result.message)
        }

        isLoading = false
        return@withContext result.isSuccessful()
    }
}