package com.think.runex.ui.workout.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.think.runex.R
import com.think.runex.common.setStatusBarColor
import com.think.runex.ui.base.BaseScreen
import com.think.runex.util.NightMode

class WorkoutHistoryScreen : BaseScreen() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_workout_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(hidden.not()){
            setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        }
    }

    private fun setupComponents(){
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
    }


}