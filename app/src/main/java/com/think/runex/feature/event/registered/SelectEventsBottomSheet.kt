package com.think.runex.feature.event.registered

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jozzee.android.core.resource.getDimension
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.base.BaseBottomSheet
import com.think.runex.common.getViewModel
import com.think.runex.common.observe
import com.think.runex.common.removeObservers
import com.think.runex.component.recyclerview.MarginItemDecoration
import com.think.runex.feature.event.data.EventRegisteredForSubmitResult
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.bottom_sheet_select_events.*

class SelectEventsBottomSheet : BaseBottomSheet() {

//    companion object {
//        @JvmStatic
//        fun newInstance() = SelectEventsBottomSheet().apply {
//            arguments = Bundle().apply {
//            }
//        }
//    }

    private lateinit var viewModel: MyEventListViewModel

    private lateinit var adapter: SelectEventsAdapter
    private lateinit var layoutManager: LinearLayoutManager //For load more in the feature!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetStyle)

        viewModel = getViewModel(MyEventListViewModel.Factory(requireContext()))
        viewModel.filterByPaymentSuccess = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_select_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        performGetMyEventsForSubmitWorkout()
    }

    private fun setupComponents() {
        //Update bottom sheet show full height of layout.
        getBottomSheetBehavior()?.apply {
            isFitToContents = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }

        //Setup recycler view
        adapter = SelectEventsAdapter()
        layoutManager = LinearLayoutManager(requireContext())
        event_list?.layoutManager = layoutManager
        event_list?.adapter = adapter
    }

    private fun subscribeUi() {
        close_button?.setOnClickListener {
            closeBottomSheet()
        }

        submit_button?.setOnClickListener {
            getOnConfirmSelectEventToSubmitListener()?.onConfirmSelectEventToSubmit(adapter.getEventSelectedList())
            closeBottomSheet()
        }

        adapter.setOnSelectionChange { selectedSize ->
            submit_button?.isEnabled = selectedSize > 0
        }

        viewModel.setOnHandleError(::errorHandler)
    }

    private fun performGetMyEventsForSubmitWorkout() = launch {
        progress_layout?.visible()
        val myEvents = viewModel.getMyEventsForSubmitWorkout()
        progress_layout?.gone()
        adapter.submitList(myEvents?.toMutableList())
    }

    override fun errorHandler(statusCode: Int, message: String, tag: String?) {
        super.errorHandler(statusCode, message, tag)
        progress_layout?.gone()
    }

    private fun getOnConfirmSelectEventToSubmitListener(): OnConfirmSelectEventToSubmitListener? {
        if (parentFragment != null && parentFragment is OnConfirmSelectEventToSubmitListener) {
            return parentFragment as OnConfirmSelectEventToSubmitListener
        } else if (activity != null && activity is OnConfirmSelectEventToSubmitListener) {
            return activity as OnConfirmSelectEventToSubmitListener
        }
        return null
    }

    interface OnConfirmSelectEventToSubmitListener {
        fun onConfirmSelectEventToSubmit(events: List<EventRegisteredForSubmitResult>)
    }

    override fun onDestroyView() {
        removeObservers(viewModel.myEvents)
        super.onDestroyView()
    }

}