package com.think.runex.feature.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jozzee.android.core.fragment.onBackPressed
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.setVisible
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.config.*
import com.think.runex.feature.activity.AddActivityScreen
import com.think.runex.feature.dashboard.data.DashboardInfo
import com.think.runex.feature.event.team.TeamManagementScreen
import com.think.runex.util.NightMode
import com.think.runex.util.extension.*
import com.think.runex.util.extension.launch
import kotlinx.android.synthetic.main.screen_dashboard.*
import kotlinx.android.synthetic.main.toolbar.*

class DashboardScreen : BaseScreen() {

    companion object {
        @JvmStatic
        fun newInstance(eventCode: String,
                        orderId: String,
                        registerId: String,
                        parentRegisterId: String) = DashboardScreen().apply {

            arguments = Bundle().apply {
                putString(KEY_EVENT_CODE, eventCode)
                putString(KEY_ORDER_ID, orderId)
                putString(KEY_REGISTER_ID, registerId)
                putString(KEY_PARENT_REGISTER_ID, parentRegisterId)
            }
        }
    }

    lateinit var viewModel: DashboardViewModel

    private lateinit var adapter: UserDashboardAdapter
    private lateinit var layoutManager: LinearLayoutManager//For load more in the feature!

    private var eventCode: String = ""
    private var orderId: String = ""
    private var registerId: String = ""
    private var parentRegisterId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Get user info for fill user id
        getUserViewModel().getUserInfoIfNotHave()

        arguments?.run {
            eventCode = getString(KEY_EVENT_CODE) ?: ""
            orderId = getString(KEY_ORDER_ID) ?: ""
            registerId = getString(KEY_REGISTER_ID) ?: ""
            parentRegisterId = getString(KEY_PARENT_REGISTER_ID) ?: ""
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
        setupToolbar(toolbar_layout, R.string.dashboard, R.drawable.ic_navigation_back)

        //Set up recycler view
        adapter = UserDashboardAdapter(users_dashboard_list, this)
        layoutManager = LinearLayoutManager(requireContext())
        users_dashboard_list?.layoutManager = layoutManager
        users_dashboard_list?.adapter = adapter
    }

    private fun subscribeUi() {

        add_activity_button?.setOnClickListener {
            getMyUserId { userId ->

            }
        }

        view_leader_board_button?.setOnClickListener {
            //addFragment(LeaderBoardScreen.newInstance(eventCode))
        }

        team_management_button?.setOnClickListener {
            addFragment(TeamManagementScreen.newInstance(eventCode, registerId, parentRegisterId))
        }

        viewModel.setOnHandleError(::errorHandler)
    }

    private fun performGetEventDashBoard() = launch {
        progress_layout?.visible()

        val dashboards = viewModel.getEventDashboard(eventCode, orderId, registerId, parentRegisterId)

        progress_layout?.gone()

        if (dashboards != null) {
            //Update Ui
            updateUi(dashboards)
        }
    }

    private fun updateUi(dashboards: DashboardInfo) {
        event_name_label?.text = dashboards.eventRegistered?.eventDetail?.title ?: ""
        total_distances_label?.text = dashboards.getTotalDistanceDisplay(getString(R.string.km))
        team_management_button?.setVisible(dashboards.isEventCategoryTeam())

        adapter.submitList(dashboards.userActivityList?.toMutableList())
    }


    private fun gotoAddActivityScreen(userId: String) {
        addFragment(AddActivityScreen())
    }

    private fun getMyUserId(callback: (userId: String) -> Unit) {
        launch {

            //Get user info from live data.
            val userId = getUserViewModel().userInfo.value?.id
            if (userId?.isNotBlank() == true) {
                callback.invoke(userId)
                return@launch
            }

            //Get user info from api.
            showProgressDialog("Get User Info")
            val userInfo = getUserViewModel().getUSerInfoInstance()
            hideProgressDialog()

            if (userInfo != null && userInfo.id?.isNotBlank() == true) {
                callback.invoke(userInfo.id ?: "")
                return@launch
            }

            //Get user info failed!.
            showAlertDialog(R.string.error, R.string.data_not_found, false) {
                onBackPressed()
            }
        }
    }

    override fun errorHandler(statusCode: Int, message: String, tag: String?) {
        progress_layout?.gone()
        when (tag) {
            "dashboard" -> showAlertDialog(getString(R.string.error), message, isCancelEnable = false) {
                onBackPressed()
            }
            else -> super.errorHandler(statusCode, message, tag)
        }
    }

    override fun onDestroyView() {
        users_dashboard_list?.recycledViewPool?.clear()
        adapter.clear()
        super.onDestroyView()
    }
}