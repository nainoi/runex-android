package com.think.runex.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jozzee.android.core.resource.getDimension
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.common.getViewModel
import com.think.runex.common.observe
import com.think.runex.common.removeObservers
import com.think.runex.common.setStatusBarColor
import com.think.runex.feature.event.MyEventListViewModel
import com.think.runex.feature.event.MyEventListViewModelFactory
import com.think.runex.ui.base.BaseScreen
import com.think.runex.ui.component.recyclerview.MarginItemDecoration
import kotlinx.android.synthetic.main.screen_my_events.*

class MyEventsScreen : BaseScreen() {

    private lateinit var viewModel: MyEventListViewModel

    private lateinit var adapter: MyEventsAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel(MyEventListViewModelFactory(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_my_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        //Initial get all event list
        progress_bar?.visible()
        viewModel.getEventList()
    }

    private fun setupComponents(){
        setStatusBarColor(isLightStatusBar = false)
        //Set update recycler view
        adapter = MyEventsAdapter()
        layoutManager = LinearLayoutManager(requireContext())
        event_list?.addItemDecoration(MarginItemDecoration(getDimension(R.dimen.space_16dp)))
        event_list?.layoutManager = layoutManager
        event_list?.adapter = adapter
    }

    private fun subscribeUi(){
        adapter.setOnItemClick { _, event ->

        }

        viewModel.setOnHandleError(::errorHandler)

        observe(viewModel.eventList) { eventList ->
            progress_bar?.gone()
            adapter.submitList(eventList?.toMutableList())
        }
    }

    override fun errorHandler(statusCode: Int, message: String) {
        super.errorHandler(statusCode, message)
        progress_bar?.gone()
    }

    override fun onDestroy() {
        removeObservers(viewModel.eventList)
        super.onDestroy()
    }
}
