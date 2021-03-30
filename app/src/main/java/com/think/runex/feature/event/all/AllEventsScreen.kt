package com.think.runex.feature.event.all

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
import com.think.runex.base.BaseScreen
import com.think.runex.component.recyclerview.MarginItemDecoration
import com.think.runex.feature.event.detail.EventDetailsScreen
import com.think.runex.util.NightMode
import kotlinx.android.synthetic.main.screen_all_events.*

class AllEventsScreen : BaseScreen() {

    private lateinit var viewModel: AllEventListViewModel

    private lateinit var adapter: AllEventsAdapter
    private lateinit var layoutManager: LinearLayoutManager//For load more in the feature!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel(AllEventListViewModel.Factory(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_all_events, container, false)
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

        //Set up recycler view
        adapter = AllEventsAdapter()
        layoutManager = LinearLayoutManager(requireContext())
        event_list?.addItemDecoration(MarginItemDecoration(getDimension(R.dimen.space_16dp)))
        event_list?.layoutManager = layoutManager
        event_list?.adapter = adapter
    }

    private fun subscribeUi() {

        refresh_layout?.setOnRefreshListener {
            viewModel.getEventList()
        }

        adapter.setOnItemClickListener { _, event ->
            addFragment(EventDetailsScreen.newInstance(event.code))
        }

        viewModel.setOnHandleError(::errorHandler)

        observe(viewModel.eventList) { eventList ->
            if (view == null || isAdded.not()) return@observe

            refresh_layout?.isRefreshing = false
            adapter.submitList(eventList?.toMutableList())
        }
    }

    override fun errorHandler(statusCode: Int, message: String, tag: String?) {
        super.errorHandler(statusCode, message, tag)
        refresh_layout?.isRefreshing = false
    }

    override fun onDestroy() {
        removeObservers(viewModel.eventList)
        super.onDestroy()
    }
}
