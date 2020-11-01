package com.think.runex.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName

import com.think.runex.R
import com.think.runex.feature.event.EventViewModel
import com.think.runex.feature.event.EventViewModelFactory
import com.think.runex.ui.base.BaseScreen
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.screen_all_events.*

class AllEventsScreen : BaseScreen() {

    private val eventViewModel: EventViewModel by lazy {
        ViewModelProvider(this, EventViewModelFactory(requireContext())).get(EventViewModel::class.java)
    }

    private val eventsAdapter: EventsAdapter by lazy { EventsAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_all_events, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupComponents()
        subscribeUi()

        //Get event list
        launch {
            val eventList = eventViewModel.getAllEvent()
            eventsAdapter.submitList(eventList)
        }
    }

    private fun onClick() {
        Toast.makeText(context, R.string.app_name, Toast.LENGTH_SHORT).show()

        // prepare usage variables
//        val intent = Intent(context, HomeScreenActivity::class.java)

        // start activity
//        startActivity(intent)
    }

    private fun setupComponents() {
        event_list.layoutManager = LinearLayoutManager(context)
        //event_list.addItemDecoration(ListItemDecoration(marginList = getDimen(R.dimen.space_8dp), withTopOfFirstItem = false))
        event_list.adapter = eventsAdapter
    }

    private fun subscribeUi() {
        eventsAdapter.setOnItemClick { position, eventId -> onClick() }

        eventViewModel.setOnHandleError { statusCode, message ->
            Logger.error(simpleName(), "Error: $statusCode, message: $message")
        }

    }


    override fun onDestroy() {
        event_list?.recycledViewPool?.clear()
        super.onDestroy()
    }
}
