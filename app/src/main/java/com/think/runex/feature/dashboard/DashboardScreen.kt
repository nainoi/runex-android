package com.think.runex.feature.dashboard

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.jozzee.android.core.fragment.onBackPressed
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.inVisible
import com.jozzee.android.core.view.setVisible
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.config.*
import com.think.runex.feature.activity.AddActivityScreen
import com.think.runex.feature.activity.data.ActivityForSubmitEvent
import com.think.runex.feature.dashboard.data.DashboardInfo
import com.think.runex.feature.dashboard.data.DeleteActivityBody
import com.think.runex.feature.event.registration.RegistrationScreen
import com.think.runex.feature.event.team.TeamManagementScreen
import com.think.runex.util.NightMode
import com.think.runex.util.extension.*
import com.think.runex.util.extension.launch
import kotlinx.android.synthetic.main.screen_dashboard.*
import kotlinx.android.synthetic.main.toolbar.*

class DashboardScreen : BaseScreen(), UserDashboardListener {

    companion object {
        @JvmStatic
        fun newInstance(
            eventCode: String,
            orderId: String,
            registerId: String,
            parentRegisterId: String
        ) = DashboardScreen().apply {

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

    private var menuEdit: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

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

        //Load initial data.
        content_layout?.inVisible()
        performGetDashBoardInfo()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden.not()) {
            setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        }
    }

    fun refreshScreen() {
        performGetDashBoardInfo()
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar_layout, R.string.dashboard, R.drawable.ic_navigation_back)

        //Set up recycler view
        adapter = UserDashboardAdapter(users_dashboard_list,  this)
        layoutManager = LinearLayoutManager(requireContext())
        users_dashboard_list?.layoutManager = layoutManager
        users_dashboard_list?.adapter = adapter
    }

    private fun subscribeUi() {

        add_activity_button?.setOnClickListener {
            gotoAddActivityScreen()
        }

        view_leader_board_button?.setOnClickListener {
            val ticketId = viewModel.getTicketAtRegister()?.id ?: ""
            addFragment(
                LeaderBoardScreen.newInstance(
                    eventCode,
                    viewModel.getEventId(),
                    registerId,
                    parentRegisterId,
                    ticketId
                )
            )
        }

        team_management_button?.setOnClickListener {
            addFragment(
                TeamManagementScreen.newInstance(
                    eventCode, registerId, parentRegisterId,
                    viewModel.getTicketAtRegister()?.id ?: "", viewModel.isTeamLeader
                )
            )
        }

        observe(getMainViewModel().refreshScreen) { screenName ->
            if (view == null || isAdded.not()) return@observe
            if (screenName == this@DashboardScreen::class.java.simpleName) {
                performGetDashBoardInfo()
            }
        }

        viewModel.setOnHandleError(::errorHandler)
    }

    override fun onDeleteActivity(
        userPosition: Int,
        activityPosition: Int,
        activityId: String,
        activityToDelete: DeleteActivityBody
    ) {
        launch {
            showProgressDialog(R.string.delete_activity)
            val dashboardInfo = viewModel.deleteActivity(activityToDelete)
            hideProgressDialog()
            if (dashboardInfo != null) {
                adapter.submitList(dashboardInfo.activityList)
            }
        }
    }

    override fun getRegistrationName(userId: String?): String {
        return viewModel.getRegistrationName(userId)
    }

    private fun performGetDashBoardInfo() = launch {

        progress_bar?.visible()
        if (viewModel.dashboardInfo == null) {
            content_layout?.inVisible()
        }

        val dashboards = viewModel.getDashboardInfo(eventCode, orderId, registerId, parentRegisterId)

        progress_bar?.gone()
        if (dashboards != null) {
            content_layout?.visible()
            setupUi(dashboards)
        }
    }

    private fun setupUi(dashboards: DashboardInfo) {

        menuEdit?.isVisible = true//dashboards.registered?.eventDetail?.isOpenEditProfile == true

        event_name_label?.text = dashboards.registered?.eventDetail?.title ?: ""
        total_distances_label?.text = dashboards.getTotalDistanceDisplay(getString(R.string.km))

        team_management_button?.setVisible(dashboards.isEventCategoryTeam())
        team_management_label.text = getString(if (viewModel.isTeamLeader) R.string.team_management else R.string.team)

        adapter.myUserId = viewModel.myUserId
        adapter.submitList(dashboards.activityList?.toMutableList())
    }


    private fun gotoAddActivityScreen() {
        val activityForSubmit = ActivityForSubmitEvent().apply {
            this.eventCode = this@DashboardScreen.eventCode
            this.orderId = this@DashboardScreen.orderId
            this.registerId = this@DashboardScreen.registerId
            this.parentRegisterId = this@DashboardScreen.parentRegisterId
            this.userId = viewModel.myUserId
            this.ticket = viewModel.getTicketAtRegister()
        }
        addFragment(AddActivityScreen.newInstance(activityForSubmit))
    }

    private fun gotoEditRegistrationScreen() {
        viewModel.getRegistrationDataForEdit()?.also { registered ->
            addFragment(RegistrationScreen.newInstance(registered))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_dashboard, menu)

        menuEdit = menu.findItem(R.id.menu_edit_registration)
        menuEdit?.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_edit_registration -> {
            gotoEditRegistrationScreen()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun errorHandler(code: Int, message: String, tag: String?) {
        progress_bar?.gone()
        when (tag) {
            "dashboard" -> showAlertDialog(getString(R.string.error), message, isCancelEnable = false) {
                onBackPressed()
            }
            else -> super.errorHandler(code, message, tag)
        }
    }

    override fun onDestroyView() {
        users_dashboard_list?.recycledViewPool?.clear()
        adapter.clear()
        super.onDestroyView()
    }
}