package com.think.runex.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.jozzee.android.core.fragment.onBackPressed
import com.jozzee.android.core.resource.getColor
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.inVisible
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.common.*
import com.think.runex.config.KEY_CODE
import com.think.runex.feature.event.EventDetailsViewModel
import com.think.runex.feature.event.EventDetailsViewModelFactory
import com.think.runex.ui.base.BaseScreen
import com.think.runex.ui.component.recyclerview.LineSeparatorItemDecoration
import com.think.runex.util.NightMode
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.screen_event_details.*
import kotlinx.android.synthetic.main.toolbar.*

class EventDetailsScreen : BaseScreen(), RegisterEventWithEBIBDialog.OnEBIBSpecifiedListener {

    companion object {
        @JvmStatic
        fun newInstance(code: String?) = EventDetailsScreen().apply {
            arguments = Bundle().apply {
                putString(KEY_CODE, code)
            }
        }
    }

    private lateinit var viewModel: EventDetailsViewModel

    private lateinit var adapter: TicketTypeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel(EventDetailsViewModelFactory(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_event_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
        performGetEventDetails(arguments?.getString(KEY_CODE) ?: "")
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar, R.string.event_detail, R.drawable.ic_navigation_back)

        //Set up recycler view
        adapter = TicketTypeAdapter()
        val lineSeparator = ContextCompat.getDrawable(requireContext(), R.drawable.line_separator_list_item)?.apply {
            setColorFilter(getColor(R.color.border))
        }
        tickets_list?.addItemDecoration(LineSeparatorItemDecoration(lineSeparator))
        tickets_list?.layoutManager = LinearLayoutManager(requireContext())
        tickets_list?.adapter = adapter
    }

    private fun subscribeUi() {
        share_button?.setOnClickListener {
            onShareEvent()
        }

        register_button?.setOnClickListener {
            //TODO("Disable for now")
            //if (viewModel.eventDetail?.partner?.partnerName == "KAO") {
            //     showDialog(RegisterEventWithEBIBDialog())
            //}
        }

        viewModel.setOnHandleError(::errorHandler)
    }

    private fun performGetEventDetails(code: String) = launch {
        showLoading()
        val isSuccess = viewModel.getEventDetail(code)
        if (isSuccess) {
            hideLoading()
            updateEventDetails()
        }
    }

    private fun updateEventDetails() {
        //Set event details to views
        event_image?.loadEventsImage(viewModel.eventDetail?.coverImage())
        event_period_label?.text = viewModel.eventDetail?.eventPeriodWithTime() ?: ""
        event_title_label?.text = viewModel.eventDetail?.title ?: ""
        event_detail_label?.text = viewModel.eventDetail?.content ?: ""
        register_button?.isEnabled = false
        adapter.submitList(viewModel.tickets?.toMutableList())

        //Set register button if event not free
        if (viewModel.eventDetail?.isFreeEvent == true) {
            register_button?.isEnabled = true
        }
        launch {
            val isRegistered = viewModel.isRegisteredEvent(arguments?.getString(KEY_CODE) ?: "")
            if (isRegistered) {
                register_button?.isEnabled = false
            }
        }
    }

    private fun onShareEvent() {
        //TODO("Disable for now")
//        val url = "${ApiConfig.PREVIEW_EVENT_URL}/"
//        val sendIntent = Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_TEXT, url)
//            type = "text/plain"
//        }
//        val shareIntent = Intent.createChooser(sendIntent, getString(R.string.share))
//        startActivity(shareIntent)
    }

    override fun onEBIBSpecified(eBib: String) {
        TODO("Disable for now")
        //performRegisterEventWithEBIB(eBib)
    }

    //TODO("Disable for now")
//    private fun performRegisterEventWithEBIB(eBib: String) = viewModel.eventDetail?.also { event ->
//        launch {
//            showProgressDialog(getString(R.string.register_event))
//            val isSuccess = viewModel.registerEventWithKoa(event, eBib)
//            hideProgressDialog()
//            if (isSuccess) {
//                showRegisterSuccessDialog()
//            }
//        }
//    }

    private fun showRegisterSuccessDialog() {
        showAlertDialog(R.string.register_event_success, isCancelEnable = false) {
            //On positive click
            onBackPressed()
        }
    }

    override fun errorHandler(statusCode: Int, message: String) {
        super.errorHandler(statusCode, message)
        progress_bar?.gone()
    }

    private fun showLoading() {
        scroll_view?.inVisible()
        register_button?.inVisible()
        progress_bar?.visible()
    }

    private fun hideLoading() {
        scroll_view?.visible()
        register_button?.visible()
        progress_bar?.gone()
    }

}