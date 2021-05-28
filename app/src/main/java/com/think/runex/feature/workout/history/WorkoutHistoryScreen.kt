package com.think.runex.feature.workout.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jozzee.android.core.resource.getDimension
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.component.recyclerview.MarginItemDecoration
import com.think.runex.feature.workout.data.WorkoutInfo
import com.think.runex.feature.workout.summary.WorkoutSummaryScreen
import com.think.runex.util.NightMode
import com.think.runex.util.extension.*
import kotlinx.android.synthetic.main.screen_workout_history.*

class WorkoutHistoryScreen : BaseScreen(),
    WorkoutHistoryDayAdapter.OnClickWorkoutListener,
    WorkoutHistoryDayAdapter.OnDeleteWorkoutListener {

    private lateinit var viewModel: WorkoutHistoryViewModel

    private lateinit var adapter: WorkoutHistoryMonthAdapter
    private lateinit var layoutManager: LinearLayoutManager//For load more in the feature!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel(WorkoutHistoryViewModel.Factory(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_workout_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
        performGetHistoryList()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden.not()) {
            setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
            performGetHistoryList()
        }
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        refresh_layout?.setColorSchemeResources(R.color.colorPrimary)

        //Set up recycler view
        adapter = WorkoutHistoryMonthAdapter(history_list, this, this)
        layoutManager = LinearLayoutManager(requireContext())
        history_list?.addItemDecoration(MarginItemDecoration(getDimension(R.dimen.space_16dp), 0, 0))
        history_list?.layoutManager = layoutManager
        history_list?.adapter = adapter


    }

    private fun subscribeUi() {
        refresh_layout?.setOnRefreshListener {
            performGetHistoryList()
        }

        viewModel.setOnHandleError(::errorHandler)
    }

    private fun performGetHistoryList(lastPosition: Int = 0) = launch {
        if (viewModel.isLoading || lastPosition > 0 && viewModel.isAllLoaded) return@launch

        if (lastPosition == 0) {
            refresh_layout?.isRefreshing = true
        }

        val historyList = viewModel.getHistoryList()

        refresh_layout?.isRefreshing = false

        if (lastPosition == 0) {
            adapter.submitList(historyList)
        } else {
            adapter.addList(historyList)
        }

        if (historyList?.size ?: 0 != viewModel.pageSize) {
            viewModel.isAllLoaded = true
        }
    }

    override fun onClickWorkout(workoutInfo: WorkoutInfo) {
        addFragment(WorkoutSummaryScreen.newInstance(workoutInfo.id ?: ""))
    }

    override fun onDeleteWorkout(
        month: Int,
        year: Int,
        monthPosition: Int,
        dayPosition: Int,
        workoutInfo: WorkoutInfo
    ) {
        launch {
            showProgressDialog(R.string.delete)
            val historyMonth = viewModel.deleteWorkout(month, year, workoutInfo)
            hideProgressDialog()
            adapter.onDeleteWorkout(monthPosition, historyMonth)
        }
    }

    override fun errorHandler(code: Int, message: String, tag: String?) {
        super.errorHandler(code, message, tag)
        refresh_layout?.isRefreshing = false
    }

    override fun onDestroyView() {
        adapter.clear()
        history_list?.recycledViewPool?.clear()
        super.onDestroyView()
    }
}