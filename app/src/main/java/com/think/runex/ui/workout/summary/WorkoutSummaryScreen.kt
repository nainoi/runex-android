package com.think.runex.ui.workout.summary

import android.os.Bundle
import android.view.*
import com.think.runex.R
import com.think.runex.common.setStatusBarColor
import com.think.runex.common.setupToolbar
import com.think.runex.config.KEY_DATA
import com.think.runex.feature.workout.WorkoutRecord
import com.think.runex.ui.base.BaseScreen
import com.think.runex.util.NightMode
import kotlinx.android.synthetic.main.toolbar.*

class WorkoutSummaryScreen : BaseScreen() {

    companion object {
        @JvmStatic
        fun newInstance(record: WorkoutRecord) = WorkoutSummaryScreen().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_DATA, record)
            }
        }
    }

    private var record: WorkoutRecord? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        record = arguments?.getParcelable(KEY_DATA)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_workout_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar, R.string.summary, R.drawable.ic_navigation_back)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_workout_summary, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_share -> {
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}