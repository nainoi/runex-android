package com.think.runex.feature.event.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.common.getViewModel
import com.think.runex.common.setStatusBarColor
import com.think.runex.common.setupToolbar
import com.think.runex.config.KEY_BODY
import com.think.runex.config.KEY_CODE
import com.think.runex.config.KEY_EVENT
import com.think.runex.feature.event.data.EventDashboard
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(DashboardViewModelFactory(requireContext()))
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
    }

    private fun subscribeUi() {

        view_leader_board_button?.setOnClickListener {
            //addFragment(LeaderBoardScreen.newInstance(eventCode))
        }

        viewModel.setOnHandleError(::errorHandler)
    }

    private fun updateUi(dashboard: EventDashboard) {
        total_distances_label?.text = dashboard.getTotalDistanceDisplay(getString(R.string.km))
    }

    private fun performGetEventDashBoard() = launch {
        progress_layout?.visible()
        val body = arguments?.getParcelable(KEY_BODY) ?: EventDashboardBody()
        val dashboard = viewModel.getEventDashboard(body)
        progress_layout?.gone()

        if (dashboard != null) {
            updateUi(dashboard)
        }
    }

    override fun errorHandler(statusCode: Int, message: String, tag: String?) {
        super.errorHandler(statusCode, message, tag)
        progress_layout?.gone()
    }
}