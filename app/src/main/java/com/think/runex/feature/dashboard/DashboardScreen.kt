package com.think.runex.feature.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.common.displayFormat
import com.think.runex.common.getViewModel
import com.think.runex.common.setStatusBarColor
import com.think.runex.common.setupToolbar
import com.think.runex.config.KEY_BODY
import com.think.runex.config.KEY_EVENT
import com.think.runex.feature.event.data.request.EventDashboardBody
import com.think.runex.util.NightMode
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.screen_dashboard.*
import kotlinx.android.synthetic.main.toolbar.*

class DashboardScreen : BaseScreen() {

    companion object {
        @JvmStatic
        fun newInstance(dashboardBody: EventDashboardBody, eventName: String) = DashboardScreen().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_BODY, dashboardBody)
                putString(KEY_EVENT, eventName)
            }
        }
    }

    lateinit var viewModel: DashboardViewModel

    private lateinit var adapter: DashboardAdapter
    private lateinit var layoutManager: LinearLayoutManager//For load more in the feature!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(DashboardViewModel.Factory(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
        performGetEventDashBoard()
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar, R.string.dashboard, R.drawable.ic_navigation_back)

        event_name_label?.text = arguments?.getString(KEY_EVENT) ?: ""

        //Set up recycler view
        adapter = DashboardAdapter(activity_info_list)
        layoutManager = LinearLayoutManager(requireContext())
        activity_info_list?.layoutManager = layoutManager
        activity_info_list?.adapter = adapter
    }

    private fun subscribeUi() {

        view_leader_board_button?.setOnClickListener {
            //addFragment(LeaderBoardScreen.newInstance(eventCode))
        }

        viewModel.setOnHandleError(::errorHandler)
    }


    private fun performGetEventDashBoard() = launch {
        progress_layout?.visible()

        val body = arguments?.getParcelable(KEY_BODY) ?: EventDashboardBody()
        val dashboards = viewModel.getEventDashboard(body)

        progress_layout?.gone()

        //Update Ui
        val totalDistances: Double = dashboards?.sumByDouble { it.totalDistance ?: 0.0 } ?: 0.0
        total_distances_label?.text = ("${totalDistances.displayFormat()} ${getString(R.string.km)}")

        adapter.submitList(dashboards?.toMutableList())
    }

    override fun errorHandler(statusCode: Int, message: String, tag: String?) {
        super.errorHandler(statusCode, message, tag)
        progress_layout?.gone()
    }
}