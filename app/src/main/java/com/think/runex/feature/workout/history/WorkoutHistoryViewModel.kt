package com.think.runex.feature.workout.history

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.think.runex.base.BaseViewModel
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.workout.WorkoutApi
import com.think.runex.feature.workout.WorkoutRepository
import com.think.runex.feature.workout.data.WorkoutHistoryMonth
import com.think.runex.feature.workout.data.WorkoutInfo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class WorkoutHistoryViewModel(private val repo: WorkoutRepository) : BaseViewModel() {

    val pageSize: Int = 40

    var isLoading: Boolean = false
        private set

    var isAllLoaded: Boolean = false

    suspend fun getHistoryList(/*lastPosition: Int = 0*/): List<WorkoutHistoryMonth>? = withContext(IO) {
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
        result.data?.forEach { historyMonth ->
            historyMonth.workouts = historyMonth.workouts?.sortedByDescending {
                it.getWorkoutDateTimeMillis()
            }?.toMutableList()
        }
        isLoading = false
        return@withContext result.data
    }


    suspend fun deleteWorkout(month: Int, year: Int, workoutInfo: WorkoutInfo): WorkoutHistoryMonth? = withContext(IO) {

        val body = JsonObject().apply {
            add("activity_info", Gson().toJsonTree(workoutInfo))
        }

        val result = repo.deleteWorkout(body)

        if (result.isSuccessful().not()) {
            onHandleError(result.code, result.message)
        }

        val historyListResult = repo.getWorkoutHistory()
        if (historyListResult.isSuccessful()) {
            val monthHistory = historyListResult.data?.find { it.month == month && it.year == year }
            if (monthHistory != null) {
                //Sort day in month
                monthHistory.workouts = monthHistory.workouts?.sortedByDescending {
                    it.getWorkoutDateTimeMillis()
                }
            }
            return@withContext monthHistory
        }

        return@withContext null
    }

    class Factory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WorkoutHistoryViewModel(
                WorkoutRepository(
                    ApiService().provideService(
                        context,
                        WorkoutApi::class.java
                    )
                )
            ) as T
        }
    }
}