package com.think.runex.feature.event.registered

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jozzee.android.core.resource.getDimension
import com.think.runex.R
import com.think.runex.util.extension.getViewModel
import com.think.runex.util.extension.observe
import com.think.runex.util.extension.removeObservers
import com.think.runex.util.extension.setStatusBarColor
import com.think.runex.base.BaseScreen
import com.think.runex.component.recyclerview.MarginItemDecoration
import com.think.runex.feature.dashboard.DashboardScreen
import com.think.runex.feature.payment.PayEventScreen
import com.think.runex.feature.event.data.RegisterStatus
import com.think.runex.feature.event.registration.RegistrationScreen
import com.think.runex.util.NightMode
import kotlinx.android.synthetic.main.screen_my_events.*

class MyEventsScreen : BaseScreen() {

    private lateinit var viewModel: MyEventListViewModel

    private lateinit var adapter: MyEventsAdapter
    private lateinit var layoutManager: LinearLayoutManager //For load more in the feature!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel(MyEventListViewModel.Factory(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_my_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        //Initial get all event list
        refresh_layout?.isRefreshing = true
        viewModel.getEventList()
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

        //Setup recycler view
        adapter = MyEventsAdapter()
        layoutManager = LinearLayoutManager(requireContext())
        event_list?.addItemDecoration(MarginItemDecoration(getDimension(R.dimen.space_16dp)))
        event_list?.layoutManager = layoutManager
        event_list?.adapter = adapter
    }

    private fun subscribeUi() {

        refresh_layout?.setOnRefreshListener {
            viewModel.getEventList()
        }

        adapter.setOnItemClickListener { registered ->
            when (registered.getRegisterStatus(0)) {
                RegisterStatus.SUCCESS -> addFragment(DashboardScreen.newInstance(eventCode = registered.getEventCode(),
                        orderId = registered.getOrderId(0),
                        registerId = registered.getRegisterId(0),
                        parentRegisterId = registered.getParentRegisterId()
                ))
                RegisterStatus.WAITING_PAY -> addFragment(PayEventScreen.newInstance(eventCode = registered.getEventCode(),
                        eventName = registered.getEventName(),
                        orderId = registered.getOrderId(0),
                        registerId = registered.getRegisterId(0),
                        ref2 = registered.ref2 ?: "",
                        totalPrice = registered.getTotalPrice()))
                RegisterStatus.WAITING_CONFIRM -> addFragment(RegistrationScreen.newInstance(registered))
            }
        }

        viewModel.setOnHandleError(::errorHandler)

        observe(viewModel.myEvents) { eventList ->
            if (view == null || isAdded.not()) return@observe

            refresh_layout?.isRefreshing = false
            adapter.submitList(eventList?.toMutableList())
        }

        observe(getMainViewModel().refreshScreen) { screenName ->
            if (view == null || isAdded.not()) return@observe
            //Initial get all event list

            if (screenName.isNullOrBlank() || screenName == this@MyEventsScreen::class.java.simpleName) {
                refresh_layout?.isRefreshing = true
                viewModel.getEventList()
            }
        }
    }

    override fun errorHandler(statusCode: Int, message: String, tag: String?) {
        super.errorHandler(statusCode, message, tag)
        refresh_layout?.isRefreshing = false
    }

    override fun onDestroy() {
        removeObservers(viewModel.myEvents)
        removeObservers(getMainViewModel().refreshScreen)
        super.onDestroy()
    }
}
