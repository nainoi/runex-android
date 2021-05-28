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
import kotlinx.coroutines.delay

class WorkoutHistoryScreen : BaseScreen() {

    private lateinit var viewModel: WorkoutHistoryListViewModel

    private lateinit var adapter: WorkoutHistoryMonthAdapter
    private lateinit var layoutManager: LinearLayoutManager//For load more in the feature!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel(WorkoutHistoryListViewModelFactory(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_workout_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        //Initial get all event list
        refresh_layout?.isRefreshing = true
        viewModel.getHistoryList()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden.not()) {
            setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        }
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        refresh_layout?.setColorSchemeResources(R.color.colorPrimary)

        //Set up recycler view
        adapter = WorkoutHistoryMonthAdapter(history_list)
        layoutManager = LinearLayoutManager(requireContext())
        history_list?.addItemDecoration(MarginItemDecoration(getDimension(R.dimen.space_16dp), 0, 0))
        history_list?.layoutManager = layoutManager
        history_list?.adapter = adapter
    }

    private fun subscribeUi() {
        refresh_layout?.setOnRefreshListener {
            viewModel.getHistoryList()
        }

        adapter.setOnItemClickListener { workoutHistory ->
            addFragment(WorkoutSummaryScreen.newInstance(workoutHistory.id ?: ""))
        }

        adapter.setOnDeleteWorkoutListener { monthPosition, workoutInfo ->
            launch {

                showProgressDialog(R.string.delete)
                val isSuccess = viewModel.deleteWorkout(monthPosition, workoutInfo)
                hideProgressDialog()
                if (isSuccess) {
                    delay(100)
                    adapter.notifyItemChanged(monthPosition)
                }
            }
        }

        viewModel.setOnHandleError(::errorHandler)

        observe(viewModel.historyList) { historyList ->
            if (view == null || isAdded.not()) return@observe

            refresh_layout?.isRefreshing = false
            adapter.submitList(historyList?.toMutableList())
        }
    }


    override fun errorHandler(code: Int, message: String, tag: String?) {
        super.errorHandler(code, message, tag)
        refresh_layout?.isRefreshing = false
    }

    override fun onDestroy() {
        removeObservers(viewModel.historyList)
        super.onDestroy()
    }

}