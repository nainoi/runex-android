package com.think.runex.ui.workout.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jozzee.android.core.resource.getDimension
import com.think.runex.R
import com.think.runex.common.getViewModel
import com.think.runex.common.observe
import com.think.runex.common.removeObservers
import com.think.runex.common.setStatusBarColor
import com.think.runex.feature.workout.WorkoutHistoryListViewModel
import com.think.runex.feature.workout.WorkoutHistoryListViewModelFactory
import com.think.runex.ui.base.BaseScreen
import com.think.runex.ui.component.recyclerview.MarginItemDecoration
import com.think.runex.util.NightMode
import kotlinx.android.synthetic.main.screen_workout_history.*

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

        viewModel.setOnHandleError(::errorHandler)

        observe(viewModel.historyList) { eventList ->
            refresh_layout?.isRefreshing = false
            adapter.submitList(eventList?.toMutableList())
        }
    }

    override fun errorHandler(statusCode: Int, message: String) {
        super.errorHandler(statusCode, message)
        refresh_layout?.isRefreshing = false
    }

    override fun onDestroy() {
        removeObservers(viewModel.historyList)
        super.onDestroy()
    }

}