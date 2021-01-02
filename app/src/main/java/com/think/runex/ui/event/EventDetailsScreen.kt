package com.think.runex.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jozzee.android.core.view.showDialog
import com.think.runex.R
import com.think.runex.common.*
import com.think.runex.config.KEY_EVENT
import com.think.runex.feature.event.EventViewModel
import com.think.runex.feature.event.EventViewModelFactory
import com.think.runex.feature.event.model.Event
import com.think.runex.ui.base.BaseScreen
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.screen_event_details.*
import kotlinx.android.synthetic.main.screen_event_details.register_button
import kotlinx.android.synthetic.main.toolbar.*

class EventDetailsScreen : BaseScreen() {

    companion object {
        @JvmStatic
        fun newInstance(event: Event) = EventDetailsScreen().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_EVENT, event)
            }
        }
    }

    private lateinit var viewModel: EventViewModel
    private var event: Event? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel(EventViewModelFactory(requireContext()))
        event = arguments?.getParcelable(KEY_EVENT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_event_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
        checkRegisteredEvent()
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = false)
        setupToolbarDarkBackground(toolbar, R.string.event_detail, R.mipmap.ic_navigation_back)

        //Set event details to views
        event_image?.loadEventsImage(event?.coverImage())
        event_period_label?.text = event?.eventPeriodWithTime() ?: ""
        event_name_label?.text = event?.name ?: ""
        event_detail_label?.setTextHtmlFormat(event?.body ?: "")
        register_button?.isEnabled = false

        //Set register button if event not free
        if (event?.isFree == true) {
            register_button?.isEnabled = true
        }
    }

    private fun subscribeUi() {
        share_button?.setOnClickListener {

        }

        register_button?.setOnClickListener {
            //TODO(Force enable to register when partner name is 'KAO' for now.")
            if (event?.partner?.partnerName == "KAO") {
                showDialog(RegisterEventWithEBIBDialog())
            }
        }

        viewModel.setOnHandleError(::errorHandler)
    }

    private fun checkRegisteredEvent() = launch {
        val isRegistered = viewModel.isRegisteredEvent(event?.id ?: "")
        register_button?.isEnabled = isRegistered.not()
    }
}