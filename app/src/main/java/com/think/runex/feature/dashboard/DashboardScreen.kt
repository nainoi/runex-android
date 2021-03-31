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
import com.think.runex.common.getViewModel
import com.think.runex.common.setStatusBarColor
import com.think.runex.common.setupToolbar
import com.think.runex.feature.event.data.request.EventDashboardBody
import com.think.runex.util.NightMode
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.screen_dashboard.*
import kotlinx.android.synthetic.main.toolbar.*

class DashboardScreen : BaseScreen() {

    companion object {
        @JvmStatic
        fun newInstance(eventCode: String,
                        eventName: String,
                        orderId: String,
                        parentRegisterId: String,
                        registerId: String) = DashboardScreen().apply {

            arguments = Bundle().apply {
                putString("eventCode", eventCode)
                putString("eventName", eventName)
                putString("orderId", orderId)
                putString("parentRegisterId", parentRegisterId)
                putString("registerId", registerId)
            }
        }
    }

    lateinit var viewModel: DashboardViewModel

    private lateinit var adapter: UserDashboardAdapter
    private lateinit var layoutManager: LinearLayoutManager//For load more in the feature!

    private var eventCode: String = ""
    private var eventName: String = ""
    private var orderId: String = ""
    private var parentRegisterId: String = ""
    private var registerId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.run {
            eventCode = getString("eventCode") ?: ""
            eventName = getString("eventName") ?: ""
            orderId = getString("orderId") ?: ""
            parentRegisterId = getString("parentRegisterId") ?: ""
            registerId = getString("registerId") ?: ""
        }

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

        event_name_label?.text = eventName

        //Set up recycler view
        adapter = UserDashboardAdapter(users_dashboard_list, this)
        layoutManager = LinearLayoutManager(requireContext())
        users_dashboard_list?.layoutManager = layoutManager
        users_dashboard_list?.adapter = adapter
    }

    private fun subscribeUi() {

        add_activity_button?.setOnClickListener {
            addFragment(AddActivityScreen())
        }

        view_leader_board_button?.setOnClickListener {
            //addFragment(LeaderBoardScreen.newInstance(eventCode))
        }

        viewModel.setOnHandleError(::errorHandler)
    }


    private fun performGetEventDashBoard() = launch {
        progress_layout?.visible()

        val dashboards = viewModel.getEventDashboard(EventDashboardBody(eventCode, orderId, parentRegisterId, registerId))

        progress_layout?.gone()

        //Update Ui
        total_distances_label?.text = dashboards?.getTotalDistanceDisplay(getString(R.string.km))
        adapter.submitList(dashboards?.userActivityList?.toMutableList())
    }

    override fun errorHandler(statusCode: Int, message: String, tag: String?) {
        super.errorHandler(statusCode, message, tag)
        progress_layout?.gone()
    }
}