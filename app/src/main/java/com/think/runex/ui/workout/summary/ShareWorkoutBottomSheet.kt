package com.think.runex.ui.workout.summary

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jozzee.android.core.view.hideKeyboard
import com.think.runex.R

class ShareWorkoutBottomSheet : BottomSheetDialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = ShareWorkoutBottomSheet().apply {
            arguments = Bundle().apply {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_share_workout, container, false)
    }

//    @SuppressLint("RestrictedApi")
//    override fun setupDialog(dialog: Dialog, style: Int) {
//        val rootView = View.inflate(context, R.layout.bottom_sheet_share_workout, null)
//        dialog.setContentView(rootView)
//        super.setupDialog(dialog, style)
//    }


}