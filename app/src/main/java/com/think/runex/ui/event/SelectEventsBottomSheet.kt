package com.think.runex.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.think.runex.R
import com.think.runex.ui.base.BaseBottomSheet
import kotlinx.android.synthetic.main.bottom_sheet_select_events.*

class SelectEventsBottomSheet : BaseBottomSheet() {

//    companion object {
//        @JvmStatic
//        fun newInstance() = SelectEventsBottomSheet().apply {
//            arguments = Bundle().apply {
//            }
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_select_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
    }

    private fun setupComponents() {
        //Update bottom sheet show full height of layout.
        getBottomSheetBehavior()?.apply {
            isFitToContents = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun subscribeUi() {
        close_button?.setOnClickListener {
            getBottomSheetBehavior()?.state = BottomSheetBehavior.STATE_HIDDEN
            //dismissAllowingStateLoss()
        }
    }
}