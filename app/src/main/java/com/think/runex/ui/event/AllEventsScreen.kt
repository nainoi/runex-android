package com.think.runex.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.think.runex.R
import com.think.runex.feature.event.AllEventsViewModel
import com.think.runex.feature.event.AllEventsViewModelFactory
import com.think.runex.ui.base.BaseScreen

class AllEventsScreen : BaseScreen() {

    private val eventViewModel: AllEventsViewModel by lazy {
        ViewModelProvider(this, AllEventsViewModelFactory(requireContext())).get(AllEventsViewModel::class.java)
    }

    //private val eventsAdapter: EventsAdapter by lazy { EventsAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_all_events, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupComponents()
//        subscribeUi()
//
    }


    private fun setupComponents() {
    }

    private fun subscribeUi() {
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}
