package com.think.runex.feature.event.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.LinearLayoutManager
import com.jozzee.android.core.resource.getDimension
import com.jozzee.android.core.text.toIntOrZero
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.inVisible
import com.jozzee.android.core.view.setVisible
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.component.recyclerview.LineSeparatorItemDecoration
import com.think.runex.config.KEY_EVENT_CODE
import com.think.runex.config.KEY_PARENT_REGISTER_ID
import com.think.runex.config.KEY_REGISTER_ID
import com.think.runex.config.KEY_TICKET
import com.think.runex.feature.auth.data.TokenManager
import com.think.runex.feature.event.data.Registered
import com.think.runex.feature.event.data.RegisteredRequestBody
import com.think.runex.feature.qr.QRCodeScannerActivity
import com.think.runex.feature.qr.QRCodeScannerContract
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.util.NightMode
import com.think.runex.util.extension.*
import com.think.runex.util.extension.launch
import kotlinx.android.synthetic.main.screen_team_management.*
import kotlinx.android.synthetic.main.toolbar.*

class TeamManagementScreen : BaseScreen() {

    companion object {
        @JvmStatic
        fun newInstance(
            eventCode: String,
            registerId: String,
            parentRegisterId: String,
            ticketId: String,
            isTeamLeader: Boolean
        ) = TeamManagementScreen().apply {
            arguments = Bundle().apply {
                putString(KEY_EVENT_CODE, eventCode)
                putString(KEY_REGISTER_ID, registerId)
                putString(KEY_PARENT_REGISTER_ID, parentRegisterId)
                putString(KEY_TICKET, ticketId)
                putBoolean("is_team_lead", isTeamLeader)
            }
        }
    }

    private lateinit var viewModel: TeamViewModel
    private lateinit var adapter: MembersAdapter
    private lateinit var qrScannerLauncher: ActivityResultLauncher<QRCodeScannerContract.Input>

    private var eventCode: String = ""
    private var eventName: String = ""
    private var registerId: String = ""
    private var parentRegisterId: String = ""
    private var ticketId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.run {
            eventCode = getString(KEY_EVENT_CODE) ?: ""
            registerId = getString(KEY_REGISTER_ID) ?: ""
            parentRegisterId = getString(KEY_PARENT_REGISTER_ID) ?: ""
            ticketId = getString(KEY_TICKET) ?: ""
        }

        viewModel = getViewModel(TeamViewModel.Factory(requireContext()))
        viewModel.isTeamLeader = arguments?.getBoolean("is_team_lead") ?: false

        qrScannerLauncher = registerForActivityResult(QRCodeScannerContract()) { output ->
            val data = output?.data ?: ""
            if (data.isNotBlank()) {
                val userId = data.substring(QRCodeScannerActivity.PREFIX_RUNEX.length, data.length)
                performGetUserInfoById(userId)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_team_management, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        //Get register data initial.
        showLoading()
        performGetTeamImage()
        viewModel.getRegisterData(RegisteredRequestBody(eventCode, registerId, parentRegisterId, ticketId))
    }

    private fun setupComponents() {

        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())

        val toolbarTitle = if (viewModel.isTeamLeader) R.string.team_management else R.string.team
        setupToolbar(toolbar_layout, toolbarTitle, R.drawable.ic_navigation_back)

        //Set up recycler view
        adapter = MembersAdapter(requireContext(), this)

        val itemDecoration = LineSeparatorItemDecoration(
            lineSeparator = getDrawable(R.drawable.line_separator_list_item),
            addLineOnBottomOrRightOfLastItem = true
        ).apply {
            marginLeftOrTop = getDimension(R.dimen.space_16dp)
            marginRightOrBottom = getDimension(R.dimen.space_16dp)
            addLineToBottomOrRightOfLastItem = false
        }

        member_list?.addItemDecoration(itemDecoration)
        member_list?.layoutManager = LinearLayoutManager(requireContext())
        member_list?.adapter = adapter

        //Update scan qr code by team leader only.
        add_member_button?.isEnabled = viewModel.isTeamLeader
        add_member_navigator_icon?.setVisible(viewModel.isTeamLeader)
    }

    private fun subscribeUi() {

        edit_team_button?.setOnClickListener {
            viewModel.getMyRegistrationData()?.also { registerData ->
                addFragment(TeamEditorScreen.newInstance(registerData))
            }
        }

        add_member_button?.setOnClickListener {
            qrScannerLauncher.launch(
                QRCodeScannerContract.Input(
                    titleText = getString(R.string.scan_qr_code_for_add_member_in_event),
                    descriptionText = eventName,
                    prefixFormat = QRCodeScannerActivity.PREFIX_RUNEX
                )
            )
        }

        adapter.setOnItemClickListener {
        }

        observe(viewModel.registerData) { registerData ->

            if (view == null || isAdded.not()) return@observe

            hideLoading()

            if (registerData != null) {
                eventName = registerData.getEventName()
                updateUi(registerData)
            }
        }

        observe(getMainViewModel().refreshScreen) { screenName ->
            if (view == null || isAdded.not()) return@observe
            if (screenName == this@TeamManagementScreen::class.java.simpleName) {
                //Get register data initial.
                showLoading()
                performGetTeamImage()
                viewModel.getRegisterData(RegisteredRequestBody(eventCode, registerId, parentRegisterId, ticketId))
            }
        }

        viewModel.setOnHandleError(::errorHandler)
    }


    private fun updateUi(registered: Registered) {

        viewModel.getLeaderRegistrationData()?.also { registerData ->

            val team = registerData.ticketOptions?.get(0)?.userOption?.team
            team_label?.text = ("${getString(R.string.team)}: $team")
            edit_team_button?.setVisible(TokenManager.userId == registerData.userId)
        }

        registered.getTicketAtRegister()?.also { ticket ->
            val memberCount = (registered.registeredDataList?.size ?: 0)

            val addMemberLabel = getString(if (viewModel.isTeamLeader) R.string.add_member else R.string.member)
            add_member_label?.text = ("$addMemberLabel ($memberCount/${ticket.runnerInTeam ?: ""})")
            add_member_button?.isEnabled = viewModel.isTeamLeader && memberCount < ticket.runnerInTeam.toIntOrZero()
        }

        adapter.submitList(registered.registeredDataList?.toMutableList())
    }

    private fun performGetUserInfoById(userId: String) = launch {

        showProgressDialog(R.string.check_information)

        val userInfo = viewModel.getUserInfoById(userId)

        hideProgressDialog()

        if (userInfo != null) {
            showAlertDialog(title = getString(R.string.add_member),
                message = getString(R.string.confirm_to_add_member, userInfo.getFullName()),
                positiveText = getString(R.string.confirm),
                negativeText = getString(R.string.cancel),
                cancelable = false,
                onPositiveClick = {
                    performAddMemberToTeam(userInfo)
                })
        }
    }

    private fun performAddMemberToTeam(userInfo: UserInfo) = launch {

        showProgressDialog(R.string.add_member)

        val isSuccess = viewModel.addMemberToTeam(userInfo)

        hideProgressDialog()

        if (isSuccess) {
            showAlertDialog(R.string.success, R.string.add_member_success)
        }
    }

    private fun performGetTeamImage() = launch {
        team_image?.loadTeamImage(viewModel.getTeamImageUrl())
    }

    private fun showLoading() {
        add_member_button?.inVisible()
        member_list_placeholder?.inVisible()
        member_list?.inVisible()
        progress_bar?.visible()
    }

    private fun hideLoading() {
        add_member_button?.visible()
        member_list_placeholder?.visible()
        member_list?.visible()
        progress_bar?.gone()
    }

    override fun errorHandler(code: Int, message: String, tag: String?) {
        super.errorHandler(code, message, tag)
        hideLoading()
    }

    override fun onDestroyView() {
        removeObservers(viewModel.registerData)
        super.onDestroyView()
    }

    override fun onDestroy() {
        member_list?.recycledViewPool?.clear()
        adapter.clear()
        removeObservers(getMainViewModel().refreshScreen)
        super.onDestroy()
    }
}