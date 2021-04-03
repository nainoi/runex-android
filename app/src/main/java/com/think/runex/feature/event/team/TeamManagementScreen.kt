package com.think.runex.feature.event.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jozzee.android.core.resource.getDimension
import com.jozzee.android.core.text.toIntOrZero
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.inVisible
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.component.recyclerview.LineSeparatorItemDecoration
import com.think.runex.config.KEY_EVENT_CODE
import com.think.runex.config.KEY_PARENT_REGISTER_ID
import com.think.runex.config.KEY_REGISTER_ID
import com.think.runex.feature.event.data.EventRegistered
import com.think.runex.util.NightMode
import com.think.runex.util.extension.*
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.screen_team_management.*
import kotlinx.android.synthetic.main.toolbar.*

class TeamManagementScreen : BaseScreen() {

    companion object {
        @JvmStatic
        fun newInstance(eventCode: String, registerId: String, parentRegisterId: String) = TeamManagementScreen().apply {
            arguments = Bundle().apply {
                putString(KEY_EVENT_CODE, eventCode)
                putString(KEY_REGISTER_ID, registerId)
                putString(KEY_PARENT_REGISTER_ID, parentRegisterId)
            }
        }
    }

    private lateinit var viewModel: TeamViewModel
    private lateinit var adapter: MembersAdapter

    private var eventCode: String = ""
    private var registerId: String = ""
    private var parentRegisterId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.run {
            eventCode = getString(KEY_EVENT_CODE) ?: ""
            registerId = getString(KEY_REGISTER_ID) ?: ""
            parentRegisterId = getString(KEY_PARENT_REGISTER_ID) ?: ""
        }

        viewModel = getViewModel(TeamViewModel.Factory(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_team_management, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        performGetRegisterData()
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar, R.string.team_management, R.drawable.ic_navigation_back)

        //Set up recycler view
        adapter = MembersAdapter(requireContext(), this)

        val itemDecoration = LineSeparatorItemDecoration(
                lineSeparator = getDrawable(R.drawable.line_separator_list_item),
                addLineOnBottomOrRightOfLastItem = true).apply {
            marginLeftOrTop = getDimension(R.dimen.space_16dp)
            marginRightOrBottom = getDimension(R.dimen.space_16dp)
            addLineToBottomOrRightOfLastItem = false
        }

        member_list?.addItemDecoration(itemDecoration)
        member_list?.layoutManager = LinearLayoutManager(requireContext())
        member_list?.adapter = adapter
    }

    private fun subscribeUi() {
        add_member_button?.setOnClickListener {

        }

        adapter.setOnItemClickListener {

        }

        viewModel.setOnHandleError(::errorHandler)
    }

    private fun performGetRegisterData() = launch {

        showLoading()

        val registerData = viewModel.getEventRegisteredInfo(eventCode, registerId, parentRegisterId)

        hideLoading()

        if (registerData != null) {
            updateUi(registerData)
        }
    }

    private fun updateUi(registerData: EventRegistered) {

        registerData.getTicketAtRegister()?.also { ticket ->
            val memberCount = (registerData.eventRegisteredList?.size ?: 0)

            add_member_label?.text = ("${getString(R.string.add_member)} ($memberCount/${ticket.runnerInTeam ?: ""})")
            add_member_button?.isClickable = memberCount < ticket.runnerInTeam.toIntOrZero()
        }

        adapter.submitList(registerData.eventRegisteredList?.toMutableList())
    }

    private fun showLoading() {
        add_member_button?.inVisible()
        member_list_placeholder?.visible()
        member_list?.inVisible()
        progress_bar?.visible()
    }

    private fun hideLoading() {
        add_member_button?.visible()
        member_list_placeholder?.visible()
        member_list?.visible()
        progress_bar?.gone()
    }

    override fun errorHandler(statusCode: Int, message: String, tag: String?) {
        super.errorHandler(statusCode, message, tag)
        hideLoading()
    }

    override fun onDestroy() {
        member_list?.recycledViewPool?.clear()
        adapter.clear()

        super.onDestroy()
    }
}