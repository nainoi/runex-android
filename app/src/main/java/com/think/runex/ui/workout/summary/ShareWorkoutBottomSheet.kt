package com.think.runex.ui.workout.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.think.runex.R
import com.think.runex.config.KEY_DATA
import com.think.runex.feature.workout.model.WorkoutInfo

class ShareWorkoutBottomSheet : BottomSheetDialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance(workoutInfo: WorkoutInfo?) = ShareWorkoutBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_DATA, workoutInfo)
            }
        }
    }

    private var workoutInfo: WorkoutInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetStyle)

        workoutInfo = arguments?.getParcelable(KEY_DATA)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_share_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()

    }

    private fun setupComponents() {
        //Update bottom sheet show full height of layout.
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            isFitToContents = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun subscribeUi() {

    }
}