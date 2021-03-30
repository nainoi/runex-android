package com.think.runex.feature.event.detail

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jozzee.android.core.fragment.onBackPressed
import com.jozzee.android.core.resource.getColor
import com.jozzee.android.core.resource.getDrawable
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.inVisible
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.common.*
import com.think.runex.config.KEY_CODE
import com.think.runex.base.BaseScreen
import com.think.runex.component.recyclerview.LineSeparatorItemDecoration
import com.think.runex.feature.event.TicketsAdapter
import com.think.runex.feature.event.data.EventDetail
import com.think.runex.feature.event.register.RegisterEventScreen
import com.think.runex.util.NightMode
import com.think.runex.util.launch
import com.think.runex.util.runOnUiThread
import kotlinx.android.synthetic.main.screen_event_details.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.delay

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

    private lateinit var adapter: TicketsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel(EventDetailsViewModel.Factory(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_event_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        showLoading()
        viewModel.getEventDetail(arguments?.getString(KEY_CODE) ?: "")
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar, R.string.event_detail, R.drawable.ic_navigation_back)

        //Set up recycler view
        adapter = TicketsAdapter()
        val lineSeparator = getDrawable(R.drawable.line_separator_list_item)?.apply {
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
            addFragment(RegisterEventScreen.newInstance(arguments?.getString(KEY_CODE) ?: ""))
        }

        viewModel.setOnHandleError(::errorHandler)

        observe(viewModel.eventDetail) { eventDetail ->
            if (view == null || isAdded.not()) return@observe
            hideLoading()
            updateEventDetails(eventDetail)
        }

        observe(getMainViewModel().refreshScreen) {
            if (view == null || isAdded.not()) return@observe
            showLoading()
            viewModel.getEventDetail(arguments?.getString(KEY_CODE) ?: "")
        }
    }

    private fun updateEventDetails(eventDetail: EventDetail?) {
        //Set event details to views
        event_image?.loadEventsImage(eventDetail?.getCoverImage())
        event_period_label?.text = eventDetail?.getEventPeriodWithTime() ?: ""
        event_title_label?.text = eventDetail?.title ?: ""
        event_detail_label?.text = eventDetail?.content ?: ""
        adapter.submitList(eventDetail?.tickets?.toMutableList())

        //Set register button
        launch {
            val isRegistered = viewModel.isRegisteredEvent(arguments?.getString(KEY_CODE) ?: "")
            setEnableRegisterButton(eventDetail?.isOpenRegister == true && isRegistered.not())
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
        //TODO("Disable for now")
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

    override fun errorHandler(statusCode: Int, message: String, tag: String?) {
        super.errorHandler(statusCode, message, tag)
        hideLoading()
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

    private fun setEnableRegisterButton(isEnabled: Boolean) = runOnUiThread {
        register_button?.isEnabled = isEnabled
        register_label?.isEnabled = isEnabled
        running_icon?.setImageDrawable(getDrawable(R.mipmap.ic_running)?.let {
            it.setColorFilter(getColor(if (isEnabled) R.color.iconColorWhite else R.color.iconColorDisable))
            it
        })
        register_button.setBackgroundResource(R.drawable.bg_btn_primary)
    }


    override fun onDestroy() {
        removeObservers(viewModel.eventDetail)
        removeObservers(getMainViewModel().refreshScreen)
        super.onDestroy()
    }
}