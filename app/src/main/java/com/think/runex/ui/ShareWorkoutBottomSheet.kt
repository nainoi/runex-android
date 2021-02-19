package com.think.runex.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.think.runex.R

class ShareWorkoutBottomSheet : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = ShareWorkoutBottomSheet().apply {
            arguments = Bundle().apply {
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_share_workout, container, false)
    }
}