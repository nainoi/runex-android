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
import com.think.runex.feature.event.AllEventListViewModel
import com.think.runex.feature.event.AllEventListViewModelFactory
import com.think.runex.ui.base.BaseScreen
import com.think.runex.ui.component.recyclerview.MarginItemDecoration
import kotlinx.android.synthetic.main.screen_all_events.*

class AllEventsScreen : BaseScreen() {

    private lateinit var viewModel: AllEventListViewModel

    private lateinit var adapter: AllEventsAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel(AllEventListViewModelFactory(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_all_events, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        //Initial get all event list
        progress_bar?.visible()
        viewModel.getEventList()
    }


    private fun setupComponents() {
        //Set update recycler view
        adapter = AllEventsAdapter()
        layoutManager = LinearLayoutManager(requireContext())
        event_list?.addItemDecoration(MarginItemDecoration(getDimension(R.dimen.space_16dp)))
        event_list?.layoutManager = layoutManager
        event_list?.adapter = adapter
    }

    private fun subscribeUi() {

        adapter.setOnItemClick { _, event ->
            //TODO("Open event details screen")
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
